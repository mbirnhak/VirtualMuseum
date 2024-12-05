import os
import requests
from flask import Flask, render_template, request, session, redirect, url_for
import json

IMAGE_URLS_FILE = 'image_urls.json'

def load_image_urls():
    """Load the stored image URLs from the file."""
    if os.path.exists(IMAGE_URLS_FILE):
        with open(IMAGE_URLS_FILE, 'r') as file:
            return json.load(file)
    return []

def save_image_urls(image_urls):
    """Save the list of image URLs to the file."""
    with open(IMAGE_URLS_FILE, 'w') as file:
        json.dump(image_urls, file)
app = Flask(__name__)
@app.route('/')
def home():
    image_urls = load_image_urls()
    return render_template('index.html', image_urls=image_urls)

@app.route('/create_image', methods=['POST'])
def create_image():
    prompt = request.form['prompt']
    data = {'prompt': prompt}
    data_json = json.dumps(data)
    url = 'http://localhost:8081/create/image'
    header = {'Content-Type': 'application/json'}
    response = requests.post(url, data=data_json, headers=header)
    image_url = response.text

    # Load existing image URLs, append the new one, and save it back to the file
    image_urls = load_image_urls()
    image_urls.append(image_url)
    save_image_urls(image_urls)

    # Returns URL of the image
    return redirect(url_for('home'))

if __name__ == '__main__':
    app.run(debug=True, port=8080)