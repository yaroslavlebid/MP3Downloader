import yt_dlp
import json

def get_video_info(url):
    ydl_opts = {
        'format': 'bestaudio/best'  # Selects the best quality audio stream
    }

    with yt_dlp.YoutubeDL(ydl_opts) as ydl:
        video_info = ydl.extract_info(url, download=False)

    # Extract desired information from the video_info dictionary
    id = video_info.get('id')
    title = video_info.get('title')
    thumbnail = video_info.get('thumbnail')
    view_count = video_info.get('view_count')
    like_count = video_info.get('like_count')
    audio_stream = video_info.get('url', None)
    result = {
        'id': id,
        'url': url,
        'title': title,
        'thumbnail': thumbnail,
        'view_count': view_count,
        'like_count': like_count,
        'audio_url': audio_stream
    }
    return json.dumps(result)