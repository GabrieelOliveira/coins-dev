package io.github.yeetzy.currencies.util;

import com.google.common.collect.Lists;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by @SrGutyerrez
 */
public abstract class Helper {

    /**
     * Check for integer
     *
     * @param o
     * @return
     */
    public static Boolean isInteger(Object o) {
        return o instanceof Integer;
    }

    /**
     * Check for integer
     *
     * @param o
     * @return
     */
    public static Boolean isLong(String o){
        try {
            Long.parseLong(o);
        } catch (Exception e) {}
        return false;
    }

    /**
     * Check for integer
     *
     * @param o
     * @return
     */
    public static Boolean isDouble(String o) {
        try {
            double d = Double.parseDouble(o);
            return true;
        } catch (Exception e) {}
        return false;
    }

    /**
     * Check for byte
     *
     * @param input
     * @return
     */
    public static Boolean isByte(String input) {
        try {
            Byte.parseByte(input);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Check for short
     *
     * @param input
     * @return
     */
    public static Boolean isShort(String input) {
        try {
            Short.parseShort(input);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Check for integer
     *
     * @param input
     * @return
     */
    public static Boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Check for float
     *
     * @param input
     * @return
     */
    public static Boolean isFloat(String input) {
        try {
            Float.parseFloat(input);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Check for string
     *
     * @param o
     * @return
     */
    public static Boolean isString(Object o) {
        return o instanceof String;
    }

    /**
     * Check for Boolean
     *
     * @param o
     * @return
     */
    public static Boolean isBoolean(Object o) {
        return o instanceof Boolean;
    }

    public static Boolean isJSONArray(Object object) {
        return object instanceof JSONArray;
    }

    public static Boolean isJSONObject(Object object) {
        return object instanceof JSONObject;
    }

    public static Boolean isURLValid(String urlString) {
        try {
            URL url = new URL(urlString);

            url.openConnection();

            return true;
        } catch (IOException exception) {
            return false;
        }
    }

    public static Boolean isValidMail(String mailAddress) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");

        Matcher matcher = pattern.matcher(mailAddress);

        return matcher.matches();
    }

    public static String colorize(String message) {
        return message.replaceAll(
                "&",
                "§"
        );
    }

    public static List<String> fromArray(String... values) {
        List<String> results = Lists.newArrayList();
        Collections.addAll(results, values);
        results.remove("");
        return results;
    }

    public static String[] removeFirst(String[] args) {
        List<String> out = Helper.fromArray(args);

        if (!out.isEmpty()) {
            out.remove(0);
        }
        return Helper.toArray(out);
    }

    public static String[] toArray(List<String> list) {
        return list.toArray(new String[list.size()]);
    }

    public static String toMessage(String[] args) {
        StringBuilder message = new StringBuilder();

        for (int i = 0; i < args.length; i++)
            message.append(args[i])
                    .append(" ");

        return message.toString();
    }

    public static String generateString(Integer length) {
        return Helper.generateString(false, length);
    }

    public static String generateString(Boolean uppercase, Integer length) {
        String characters = (uppercase ? "ABCDEFGHIJKLMNOPQRSTUVXYWZ" : "") + "abcdefghijklmnopqrstuvxywz0123456789";

        char[] array = characters.toCharArray();

        StringBuilder stringBuilder = new StringBuilder();

        Random random = new Random();

        for (int i = 0; i < length; i++) {
            Integer index = random.nextInt(array.length);

            char character = array[index];

            stringBuilder.append(character);
        }

        return stringBuilder.toString();
    }

    public static String hash(String sampleText, String algorithm) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

            messageDigest.update(sampleText.getBytes());

            byte[] bytes = messageDigest.digest();
        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static String hash(String sampleText) {
        return Helper.hash(sampleText, "MD5");
    }

    public static Boolean compare(String password, String hashedPassword) {
        if (!hashedPassword.contains("$")) return Helper.hash(password).equals(hashedPassword);

        String[] shaInfo = hashedPassword.split("\\$");

        String salt = shaInfo[2].split("@")[1];

        String hash = shaInfo[2].split("@")[0];

        String password1 = Helper.hash(password, "sha-256") + salt;

        String password2 = Helper.hash(password1, "sha-256");

        return hash.equals(password2);
    }

    public static String capitalize(String text) {
        return String.valueOf(text.charAt(0)).toUpperCase() + text.substring(1);
    }
}