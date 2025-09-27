from PIL import Image
import os

def combine_frames_to_gif(frames_folder, output_gif, duration=80, loop=0):
    """
    รวมภาพ PNG เป็น GIF
    
    frames_folder : โฟลเดอร์ที่มีไฟล์ frame_XXX.png
    output_gif : ชื่อไฟล์ GIF ที่จะสร้าง
    duration : ความเร็วของแต่ละเฟรม (milliseconds)
    loop : จำนวนครั้งที่วนซ้ำ (0 = วนตลอด)
    """
    frames = []
    
    # หาไฟล์ frame ทั้งหมด
    frame_files = []
    for filename in os.listdir(frames_folder):
        if filename.startswith("emote_0") and filename.endswith(".png"):
            frame_files.append(filename)
    
    # เรียงลำดับไฟล์
    frame_files.sort()
    
    print(f"พบไฟล์ frame: {len(frame_files)} ไฟล์")
    
    # โหลดแต่ละเฟรม
    for frame_file in frame_files:
        frame_path = os.path.join(frames_folder, frame_file)
        try:
            frame = Image.open(frame_path)
            frames.append(frame)
            print(f"โหลด: {frame_file}")
        except Exception as e:
            print(f"ไม่สามารถโหลด {frame_file}: {e}")
    
    if not frames:
        print("ไม่พบไฟล์ frame!")
        return
    
    # สร้าง GIF
    print(f"กำลังสร้าง GIF: {output_gif}")
    frames[0].save(
        output_gif,
        save_all=True,
        append_images=frames[1:],
        duration=duration,
        loop=loop,
        disposal=2
    )
    
    print(f"✅ สร้าง GIF สำเร็จ: {output_gif}")
    print(f"📊 จำนวนเฟรม: {len(frames)}")
    print(f"⏱️ ความเร็ว: {duration}ms/เฟรม")

# ตัวอย่างการใช้งาน
if __name__ == "__main__":
    # รวมเฟรมจากโฟลเดอร์ frame เป็น GIF
    combine_frames_to_gif(
        frames_folder="./pa",
        output_gif="../assets/background/animated_background.gif",
        duration=200,
        loop=0
    )
