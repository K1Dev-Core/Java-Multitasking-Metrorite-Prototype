from PIL import Image

def remove_black_bg(input_path, output_path):

    img = Image.open(input_path)
    frames = []

   
    for frame in range(img.n_frames):
        img.seek(frame)
        frame_img = img.convert("RGBA")
        datas = frame_img.getdata()

        new_data = []
        for item in datas:
          
            if item[0] < 30 and item[1] < 30 and item[2] < 30: 
                new_data.append((0, 0, 0, 0))  
            else:
                new_data.append(item)

        frame_img.putdata(new_data)
        frames.append(frame_img)


    frames[0].save(
        output_path,
        save_all=True,
        append_images=frames[1:],
        loop=0,
        disposal=2,
        transparency=0
    )


remove_black_bg("3.gif", "output.gif")
