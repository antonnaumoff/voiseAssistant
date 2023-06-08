import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

import java.io.IOException;
import java.util.Arrays;

public class VoiceAssistant {
    //declaring voice
    private static final String voice = "kevin16";

    private static Configuration config;

    private static LiveSpeechRecognizer speech;

    private static Voice v;

    public static void main(String[] st) throws IOException {

        VoiceManager voiceManager = VoiceManager.getInstance();
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        v = voiceManager.getVoice(voice);//assigning voice in the Voice type
        v.allocate();

        //configuration for the listening your voice
        config = new Configuration();

        config.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");//hard coded path
        config.setDictionaryPath("src\\main\\resources\\1841.dic");//need to make file from sphinx knowledge base convertor
        config.setLanguageModelPath("src\\main\\resources\\1841.lm");

        speech = new LiveSpeechRecognizer(config);

        String[] products = {"blenders", "toasters", "mixers"};
        String[] producers = {"bosch", "braun", "electrolux", "philips", "tefal"};
        String[] order = {"expensive", "cheap", "novelty", "rank"};

        v.speak("I am the best voice assistant, let's search Rozetka shop.");

        v.speak("Please choose the product from the list:");

        for (String product : products) {
            v.speak(product);
            System.out.println(product);
        }

        String product = getVoiceInput(products);

        v.speak("Please choose the producer from the list:");

        for (String producer : producers) {
            v.speak(producer);
            System.out.println(producer);
        }

        String producer = getVoiceInput(producers);

        v.speak("Please choose the search order:");

        for (String o : order) {
            v.speak(o);
            System.out.println(o);
        }

        String sort = getVoiceInput(order);

        String url = String.format("https://bt.rozetka.com.ua/%s/c80156/producer=%s;sort=%s/", product, producer, sort);

        Runtime.getRuntime().exec("cmd.exe /c start chrome " + url);


    }

    private static String getVoiceInput(String[] commands) {

        String command = null;

            //making system understand you are speaking

            speech.startRecognition(true);

            SpeechResult speechResult;
            v.speak("I am listening...");
            System.out.println("listening");
            //running until user is speaking
            while ((speechResult = speech.getResult()) != null) {
                String voiceCommand = speechResult.getHypothesis();
                System.out.println("Voice Command is " + voiceCommand);

                if (Arrays.asList(commands).contains(voiceCommand.toLowerCase())) {
                    command = voiceCommand;
                    v.speak(command);
                    break;
                } else if (voiceCommand.equalsIgnoreCase("oliver terminate")) {
                    command = "oliver shutting down";
                    v.speak(command);
                    System.exit(0);
                } else {
                    v.speak("cannot understand please repeat");
                }
            }
        speech.stopRecognition();
        return command;
    }
}

