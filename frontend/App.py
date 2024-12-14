import base64
import os
import requests
from flask import Flask, render_template, request, redirect, url_for, send_from_directory
import json

IMAGE_URLS_FILE = 'data/image_urls.json'
IMAGE_DESCRIPTIONS_FILE = 'data/image_descriptions.json'
AUDIO_FILES_DIR = 'data/audio_files'

def load_file_data(file_name):
    """Load the stored image URLs from the file."""
    if os.path.exists(file_name):
        with open(file_name, 'r') as file:
            return json.load(file)
    return []


def save_file_data(data, file_name):
    """Save the list of image URLs to the file."""
    with open(file_name, 'w') as file:
        json.dump(data, file)


def save_audio_from_base64(base64_audio, file_name):
    """Decode the Base64 audio and save it as a file."""
    # Ensure the audio files directory exists
    os.makedirs(AUDIO_FILES_DIR, exist_ok=True)

    audio_data = base64.b64decode(base64_audio)  # Decode the Base64 audio
    audio_path = os.path.join(AUDIO_FILES_DIR, file_name)

    with open(audio_path, 'wb') as audio_file:
        audio_file.write(audio_data)

    return audio_path
app = Flask(__name__)
@app.route('/')
def home():
    image_urls = load_file_data(IMAGE_URLS_FILE)
    descriptions = load_file_data(IMAGE_DESCRIPTIONS_FILE)
    images_with_descriptions = zip(image_urls, descriptions)
    return render_template('index.html', images_with_descriptions=images_with_descriptions)

@app.route('/data/audio_files/<filename>')
def serve_data_file(filename):
    # Serve the file from the data/audio_files directory
    return send_from_directory(os.path.join(app.root_path, 'data', 'audio_files'), filename)

@app.route('/create_image', methods=['POST'])
def create_image():
    for i in range(0, 1):
        prompt = request.form['prompt']
        data = {'prompt': prompt}
        data_json = json.dumps(data)
        url = 'http://localhost:8081/create/image'
        header = {'Content-Type': 'application/json'}
        response = requests.post(url, data=data_json, headers=header)
        response_data = response.json()

        image_url = response_data.get('imageUrl')
        description = response_data.get('description')
        audio_base64 = response_data.get('audioBase64')

        if audio_base64:
            audio_filename = f"audio_{i}.mp3"
            audio_path = save_audio_from_base64(audio_base64, audio_filename)

        # Load existing image URLs & descriptions
        image_urls = load_file_data(IMAGE_URLS_FILE)
        descriptions = load_file_data(IMAGE_DESCRIPTIONS_FILE)

        # Append new image & description
        image_urls.append(image_url)
        descriptions.append(description)

        # Save it back to the files
        save_file_data(image_urls, IMAGE_URLS_FILE)
        save_file_data(descriptions, IMAGE_DESCRIPTIONS_FILE)

    # Returns URL of the image
    return redirect(url_for('home'))

if __name__ == '__main__':
    app.run(debug=True, port=8080)