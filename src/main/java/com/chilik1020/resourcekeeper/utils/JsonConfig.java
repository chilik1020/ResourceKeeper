package com.chilik1020.resourcekeeper.utils;

import com.chilik1020.resourcekeeper.utils.data.BotSettings;
import com.chilik1020.resourcekeeper.utils.data.Command;
import com.chilik1020.resourcekeeper.utils.data.TempChanel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.chilik1020.resourcekeeper.utils.data.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class JsonConfig {

    public static String currentLocation = new File("").getAbsolutePath();

    public static Boolean proxyEnable;
    public static String proxyAddress;
    public static Integer proxyPort;

    public static List<BotSettings> bots = new ArrayList();
    public static List<User> allowedUsers = new ArrayList();

    public static Long shotsInCycle;
    public static Long sleepCycle;
    public static Long periodShotMs;
    public static String alarmSound;
    public static Double energyCommonMin;
    public static Double energyCommonMax;

    public static Long tempReadingPeriod;
    public static String tempLogDefaultPath;
    public static Boolean innerSensorsEn;
    public static String tempInnerSensorsLogPath;
    public static Boolean owenEnable;
    public static List<TempChanel> tempChanelsOwen = new ArrayList<TempChanel>();
    public static List<TempChanel> tempChanelsLtu = new ArrayList<TempChanel>();

    public static String screenshotPath = currentLocation + "\\screenshot.png";
    public static String pictureDirectory;

    private static final Charset UTF8_CHARSET = StandardCharsets.UTF_8;

    public static void readConfig() {

        String configPath = currentLocation + "/config.json";
        FileReader reader = null;
        try {
            reader = new FileReader(configPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) jsonParser.parse(reader);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        configProxy(jsonObject);

        configBots(jsonObject);

        configAllowedUsers(jsonObject);

        configEnergyAndTempSets(jsonObject);

        configOwenTempChanels(jsonObject);


        for (int i = 1; i < 4; i++) {
            TempChanel tempChanel = new TempChanel(i, "T"+ i, true, 18, 22);
            tempChanelsLtu.add(tempChanel);
        }
    }

    private static void configProxy(JSONObject jsonObject) {
        try {
            proxyEnable = (Boolean)jsonObject.get("proxyEnable");
            proxyAddress = (String)jsonObject.get("proxyAddress");
            Long proxyPortLong = (Long)jsonObject.get("proxyPort");
            proxyPort = Math.toIntExact(proxyPortLong);
        } catch (Exception ex) {
            System.out.println("Ошибка в файле config.json, proxy settings");
            ex.printStackTrace();
        }
    }

    private static void configEnergyAndTempSets(JSONObject jsonObject) {
        try {
            shotsInCycle = (Long)jsonObject.get("shotsInCycle");
            sleepCycle = (Long)jsonObject.get("cycleSleep");
            periodShotMs = (Long)jsonObject.get("periodShotMs");
            alarmSound = currentLocation + "\\" + (String)jsonObject.get("alarmSound");

            energyCommonMin = (Double) jsonObject.get("energyCommonMin");
            energyCommonMax = (Double) jsonObject.get("energyCommonMax");

            pictureDirectory = (String)jsonObject.get("pictureDirectory");

            tempReadingPeriod = (Long)jsonObject.get("tempReadingPeriod");
            tempLogDefaultPath = (String)jsonObject.get("temperatureLogDefaultPath");

            innerSensorsEn = (Boolean)jsonObject.get("tempInnerSensorsEnable");
            tempInnerSensorsLogPath = (String)jsonObject.get("tempInnerSensorsLogPath");
            owenEnable = (Boolean)jsonObject.get("tempOwenEnable");
        } catch (Exception ex) {
            System.out.println("Ошибка в файле config.json");
            ex.printStackTrace();
        }
    }

    private static void configBots(JSONObject jsonObject) {
        try {
            JSONArray botsJson = (JSONArray) jsonObject.get("bots");
            botsJson.forEach(b -> {
                String name = (String) ((JSONObject) b).get("name");
                String token = (String) ((JSONObject) b).get("token");
                List<Command> commands = new ArrayList();

                ((JSONArray) ((JSONObject) b).get("commands")).forEach(c -> {
                    String nameCommand = (String) ((JSONObject) c).get("name");
                    String path = (String) ((JSONObject) c).get("path");
                    commands.add(new Command(nameCommand, path));
                });
                commands.add(new Command("Screen", screenshotPath));
                commands.add(new Command("Reset shift", "ок"));
                bots.add(new BotSettings(name, token, commands));
            });
        }catch (Exception ex) {
            System.out.println("Ошибка в файле config.json, bots");
            ex.printStackTrace();
        }
    }

    private static void configAllowedUsers(JSONObject jsonObject) {
        try {
            JSONArray usersJson = (JSONArray) jsonObject.get("users");
            usersJson.forEach(u -> {
                String name = (String) ((JSONObject) u).get("name");
                Long chatId = (Long) ((JSONObject)u).get("chatId");

                allowedUsers.add(new User(name, chatId));
            });
        }catch (Exception ex) {
            System.out.println("Ошибка в файле config.json, users");
            ex.printStackTrace();
        }
    }

    private static void configOwenTempChanels(JSONObject jsonObject) {
        try {
            JSONArray tempChanelsJson = (JSONArray) jsonObject.get("tempChanels");
            tempChanelsJson.forEach(t -> {
                long number = (Long)((JSONObject)t).get("number");
                String name = (String)((JSONObject)t).get("name");

                boolean isEnable = (Boolean)((JSONObject)t).get("enable");
                double limitMin = (Double) ((JSONObject)t).get("limitMin");
                double limitMax = (Double) ((JSONObject)t).get("limitMax");
                tempChanelsOwen.add(new TempChanel(number, decodeUTF8(name.getBytes()), isEnable, limitMin, limitMax));
            });
        } catch (Exception ex) {
            System.out.println("Ошибка в файле config.json, tempChanels");
            ex.printStackTrace();
        }
    }

    private static String decodeUTF8(byte[] bytes) {
        return new String(bytes, UTF8_CHARSET);
    }
}
