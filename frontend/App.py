import requests
from flask import Flask, render_template, request, session, redirect, url_for
import json
import secrets

app = Flask(__name__)
app.secret_key = secrets.token_hex(32)  # Generates a random secret key (64 characters long)
@app.route('/')
def home():
    return render_template('index.html', image_urls=session.get('image_urls', []))

@app.route('/create_image', methods=['POST'])
def create_image():
    prompt = request.form['prompt']
    data = {'prompt': prompt}
    data_json = json.dumps(data)
    url = 'http://localhost:8081/create/image'
    header = {'Content-Type': 'application/json'}
    response = requests.post(url, data=data_json, headers=header)
    image_url = response.text

    # Store the image URL in the session (if it's not already stored)
    if 'image_urls' not in session:
        session['image_urls'] = []

    session['image_urls'].append(image_url)  # Append new image URL to the list
    print(session['image_urls'])
    # Returns URL of the image
    return redirect(url_for('home'))

if __name__ == '__main__':
    app.run(debug=True, port=8080)