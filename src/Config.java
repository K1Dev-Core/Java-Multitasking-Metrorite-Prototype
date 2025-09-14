
public class Config {
    // ขนาดหน้า
    public static int WINDOW_WIDTH = 500;
    public static int WINDOW_HEIGHT = 700;

    public static int ASTEROID_SIZE = 60; // ขนาดอุกบบาต
    public static int BOMB_SIZE = 100; // ขนาดระเบิด
    public static int BOMB_DURATION = 200; // ระยะเวลาที่ระเบิด
    public static int GAME_SPEED = 30; // ความเร็วของเกม 30 fps

    // การตั้งค่าดาวเคราะห์น้อย
    public static int MAX_ASTEROIDS = 300; // จำนวนสูงสุด
    public static int MIN_ASTEROIDS = 1; // จำนวนขั้นต่ำ
    public static int DEFAULT_ASTEROIDS = 12;
    public static int MAX_SPEED = 15;
    public static double BOUNCE_MULTIPLIER = 1.5; // คูณความเร็วตอนเด้งกลับ
    public static int COLLISION_DISTANCE = 40; //
}
