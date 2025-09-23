from PIL import Image
import os

def resize_gif(input_path, output_path, size):
   
    img = Image.open(input_path)
    frames = []

    for frame in range(img.n_frames):
        img.seek(frame)
        frame_img = img.convert("RGBA")
        frame_resized = frame_img.resize(size, Image.Resampling.LANCZOS)  # ย่อแบบคุณภาพดี
        frames.append(frame_resized)

    frames[0].save(
        output_path,
        save_all=True,
        append_images=frames[1:],
        loop=0,
        disposal=2,
        duration=img.info.get("duration", 100) 
    )

def batch_resize_gifs(input_folder, output_folder, size):

    if not os.path.exists(output_folder):
        os.makedirs(output_folder)

    for file in os.listdir(input_folder):
        if file.lower().endswith(".gif"):
            input_path = os.path.join(input_folder, file)
            output_path = os.path.join(output_folder, file)
            print(f"กำลังย่อ: {file}")
            resize_gif(input_path, output_path, size)
    print("เสร็จสิ้นทุกไฟล์แล้ว")


if __name__ == "__main__":
    input_dir = r"C:\Users\uSeR\Downloads\Java-Multitasking-Metrorite-d19088415a317a7fbcae6a54008ba8a436ee1b8c\assets\images"  
    output_dir = r"C:\Users\uSeR\Downloads\Java-Multitasking-Metrorite-d19088415a317a7fbcae6a54008ba8a436ee1b8c\assets\images" 
    target_size = (64, 64) 

    batch_resize_gifs(input_dir, output_dir, target_size)
