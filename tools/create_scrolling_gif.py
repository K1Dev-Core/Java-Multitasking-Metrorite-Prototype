from PIL import Image, ImageFilter

def create_seamless_scrolling_gif(image_path, output_path, frame_count=120, speed_x=1, speed_y=1, loop=0, duration=80, feather=0.15):
    """
    image_path : path ของภาพ input
    output_path : path gif ที่จะบันทึก
    frame_count : จำนวนเฟรม
    speed_x : pixel ที่เลื่อนในแกน X ต่อเฟรม (ค่าบวก = ขวา, ค่าลบ = ซ้าย)
    speed_y : pixel ที่เลื่อนในแกน Y ต่อเฟรม (ค่าบวก = ลง, ค่าลบ = ขึ้น)
    loop : 0 = เล่นวนตลอด
    duration : ms ต่อเฟรม
    feather : สัดส่วนของขอบที่จะเบลอ (0.0-0.5) เช่น 0.15 = 15%
    """
    img = Image.open(image_path).convert("RGBA")
    width, height = img.size
    frames = []

    # สร้าง mask เบลอ (ใช้ทำ soft edge)
    feather_px = int(min(width, height) * feather)
    mask = Image.new("L", (width, height), 255)
    mask = mask.filter(ImageFilter.GaussianBlur(feather_px))

    for i in range(frame_count):
        offset_x = (i * speed_x) % width
        offset_y = (i * speed_y) % height

        # 1) ทำ canvas เต็มก่อนด้วยการ tile
        base = Image.new("RGBA", (width, height))
        for dx in (-width, 0, width):
            for dy in (-height, 0, height):
                base.paste(img, (dx - offset_x, dy - offset_y))

        # 2) ทำ layer ซ้อนที่เบลอขอบ
        overlay = base.filter(ImageFilter.GaussianBlur(feather_px))

        # 3) blend ให้ขอบเนียน
        final = Image.composite(overlay, base, mask)

        frames.append(final)

    # บันทึก GIF
    frames[0].save(
        output_path,
        save_all=True,
        append_images=frames[1:],
        duration=duration,
        loop=loop,
        disposal=2
    )
    print(f"✅ GIF saved: {output_path}")


# 🚀 ตัวอย่าง: เลื่อนขวา+ขึ้นช้าๆ พร้อมเบลอรอยต่อ
create_seamless_scrolling_gif("Background_space.png", "space_scroll_blend_fixed.gif",
                              frame_count=180, speed_x=1, speed_y=-1, duration=80, feather=0.2)
