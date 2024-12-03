# # The 'requests' and 'json' libraries are imported.
# # 'requests' is used to send HTTP requests, while 'json' is used for parsing the JSON data that we receive from the API.
# import requests
# import json

# # An API key is defined here. You'd normally get this from the service you're accessing. It's a form of authentication.
# XI_API_KEY = "sk_8671dfe6a49e38639a819a5679e91b49635e07f9a0d76311"

# # This is the URL for the API endpoint we'll be making a GET request to.
# url = "https://api.elevenlabs.io/v1/voices"

# # Here, headers for the HTTP request are being set up.
# # Headers provide metadata about the request. In this case, we're specifying the content type and including our API key for authentication.
# headers = {
#   "Accept": "application/json",
#   "xi-api-key": XI_API_KEY,
#   "Content-Type": "application/json"
# }

# # A GET request is sent to the API endpoint. The URL and the headers are passed into the request.
# response = requests.get(url, headers=headers)

# # The JSON response from the API is parsed using the built-in .json() method from the 'requests' library.
# # This transforms the JSON data into a Python dictionary for further processing.
# data = response.json()

# # A loop is created to iterate over each 'voice' in the 'voices' list from the parsed data.
# # The 'voices' list consists of dictionaries, each representing a unique voice provided by the API.
# for voice in data['voices']:
#   # For each 'voice', the 'name' and 'voice_id' are printed out.
#   # These keys in the voice dictionary contain values that provide information about the specific voice.
#   print(f"{voice['name']}; {voice['voice_id']}")

# Import necessary libraries
import requests  # Used for making HTTP requests
import json  # Used for working with JSON data

# Define constants for the script
CHUNK_SIZE = 1024  # Size of chunks to read/write at a time
XI_API_KEY = "sk_8671dfe6a49e38639a819a5679e91b49635e07f9a0d76311"  # Your API key for authentication
VOICE_ID = "CwhRBWXzGAHq8TQ4Fs17"  # ID of the voice model to use
TEXT_TO_SPEAK = "testing the audio part of our project"  # Text you want to convert to speech
OUTPUT_PATH = "output.mp3"  # Path to save the output audio file

# Construct the URL for the Text-to-Speech API request
tts_url = f"https://api.elevenlabs.io/v1/text-to-speech/{VOICE_ID}/stream"

# Set up headers for the API request, including the API key for authentication
headers = {
    "Accept": "application/json",
    "xi-api-key": XI_API_KEY
}

# Set up the data payload for the API request, including the text and voice settings
data = {
    "text": TEXT_TO_SPEAK,
    "model_id": "eleven_multilingual_v2",
    "voice_settings": {
        "stability": 0.5,
        "similarity_boost": 0.8,
        "style": 0.0,
        "use_speaker_boost": True
    }
}

# Make the POST request to the TTS API with headers and data, enabling streaming response
response = requests.post(tts_url, headers=headers, json=data, stream=True)

# Check if the request was successful
if response.ok:
    # Open the output file in write-binary mode
    with open(OUTPUT_PATH, "wb") as f:
        # Read the response in chunks and write to the file
        for chunk in response.iter_content(chunk_size=CHUNK_SIZE):
            f.write(chunk)
    # Inform the user of success
    print("Audio stream saved successfully.")
else:
    # Print the error message if the request was not successful
    print(response.text)