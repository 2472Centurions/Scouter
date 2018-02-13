import java.awt.GraphicsEnvironment;
class SystemProperties {
    public static void main(String[] args) {
        System.getProperties().list(System.out);
        System.out.println(""+System.getProperty("os.name"));
        String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for ( int i = 0; i < fonts.length; i++ )
        {
            //System.out.println(fonts[i]);
        }
    }
}