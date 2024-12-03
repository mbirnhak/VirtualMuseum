# # Import necessary libraries
# import requests  # Used for making HTTP requests
# import json  # Used for working with JSON data

# # Define constants for the script
# CHUNK_SIZE = 1024  # Size of chunks to read/write at a time
# XI_API_KEY = "sk_8671dfe6a49e38639a819a5679e91b49635e07f9a0d76311"  # Your API key for authentication
# VOICE_ID = "CwhRBWXzGAHq8TQ4Fs17"  # ID of the voice model to use
# TEXT_TO_SPEAK = "testing the audio part of our project"  # Text you want to convert to speech
# OUTPUT_PATH = "output.mp3"  # Path to save the output audio file

# # Construct the URL for the Text-to-Speech API request
# tts_url = f"https://api.elevenlabs.io/v1/text-to-speech/{VOICE_ID}/stream"

# # Set up headers for the API request, including the API key for authentication
# headers = {
#     "Accept": "application/json",
#     "xi-api-key": XI_API_KEY
# }

# # Set up the data payload for the API request, including the text and voice settings
# data = {
#     "text": TEXT_TO_SPEAK,
#     "model_id": "eleven_multilingual_v2",
#     "voice_settings": {
#         "stability": 0.5,
#         "similarity_boost": 0.8,
#         "style": 0.0,
#         "use_speaker_boost": True
#     }
# }

# # Make the POST request to the TTS API with headers and data, enabling streaming response
# response = requests.post(tts_url, headers=headers, json=data, stream=True)

# # Check if the request was successful
# if response.ok:
#     # Open the output file in write-binary mode
#     with open(OUTPUT_PATH, "wb") as f:
#         # Read the response in chunks and write to the file
#         for chunk in response.iter_content(chunk_size=CHUNK_SIZE):
#             f.write(chunk)
#     # Inform the user of success
#     print("Audio stream saved successfully.")
# else:
#     # Print the error message if the request was not successful
#     print(response.text)

# Import necessary libraries
import requests  # Used for making HTTP requests
import json  # Used for working with JSON data

# Define constants for the script
CHUNK_SIZE = 1024  # Size of chunks to read/write at a time
XI_API_KEY = "sk_8671dfe6a49e38639a819a5679e91b49635e07f9a0d76311"  # Your API key for authentication
VOICE_ID = "CwhRBWXzGAHq8TQ4Fs17"  # ID of the voice model to use
OUTPUT_PATH = "output.mp3"  # Path to save the output audio file

# Construct the URL for the Text-to-Speech API request
tts_url = f"https://api.elevenlabs.io/v1/text-to-speech/{VOICE_ID}/stream"

# Set up headers for the API request, including the API key for authentication
headers = {
    "Accept": "application/json",
    "xi-api-key": XI_API_KEY
}

def get_image_description(image_path):
    """
    This function is a placeholder for LLM that generates
    descriptions from an image.
    Replace this with the actual call to LLM system.
    """
    # Mock response for demonstration
    return "This is a placeholder description of the image."

def text_to_speech(text):
    """
    Converts text to speech and saves the output as an audio file.
    """
    # Set up the data payload for the API request, including the text and voice settings
    data = {
        "text": text,
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
        print("Audio stream saved successfully.")
    else:
        print(f"Error: {response.status_code}")
        print(response.text)

if __name__ == "__main__":
    # Example image file path (replace with actual path)
    image_path = "example_image.jpg"

    # Get the description from the LLM
    description = get_image_description(image_path)
    print(f"Image Description: {description}")

    # Convert the description to speech
    text_to_speech(description)
