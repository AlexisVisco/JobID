package util;

/**
 * Par Alexis le 27/11/2016.
 */

public class PathTranslator
{

    private PathTranslator(){}

    public static String getPathForAsset(String s)
    {
        String replace = s.replace("\\", "/")
                .replace("public/", "");
        return replace;
    }

}
