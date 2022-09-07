package lua

class ColorCodeGenerator {

    public static String getColorCode(){
        Random random = new Random()

        // create a big random number - maximum is ffffff (hex) = 16777215 (dez)
        int nextInt = random.nextInt(0xffffff + 1)

        // format it as hexadecimal string (with hashtag and leading zeros)
        String colorCode = String.format("#%06x", nextInt)

        // print it
        return  colorCode
    }
}
