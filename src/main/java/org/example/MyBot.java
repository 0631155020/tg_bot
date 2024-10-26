package org.example;

import net.thauvin.erik.crypto.CryptoPrice;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MyBot extends TelegramLongPollingBot {
    public MyBot(){
        super("7835308914:AAFePmaHKa4VXZ0nMOHnFsyEWduMOJqfczY");
    }
    @Override
    public void onUpdateReceived(Update update) {
        var chatId = update.getMessage().getChatId();
        var text = update.getMessage().getText();

        try {
            var massage = new SendMessage();
            massage.setChatId(chatId);
        if (text.equals("/start")){
            sendMassage(chatId, "Good morning");
        }else if (text.equals("btc")){
            sendPrice(chatId, "BTC");
            sendPicture(chatId, "BTC.png");
        }else if (text.equals("eth")){
            sendPrice(chatId, "ETH");
            sendPicture(chatId, "eth.jpg");
        }else if (text.equals("doge")){
            sendPrice(chatId, "DOGE");
            sendPicture(chatId, "doge.jpg");
        } else if (text.equals("/all")){
            sendPrice(chatId, "BTC");
            sendPicture(chatId, "BTC.png");
            sendPrice(chatId, "ETH");
            sendPicture(chatId, "eth.jpg");
            sendPrice(chatId, "DOGE");
            sendPicture(chatId, "doge.jpg");
        } else if (isNumeric(text)){
            double sum = Double.parseDouble(text);

            var priceDOGE = CryptoPrice.spotPrice("DOGE").getAmount().doubleValue();
            var priceETH = CryptoPrice.spotPrice("ETH").getAmount().doubleValue();
            var priceBTC = CryptoPrice.spotPrice("BTC").getAmount().doubleValue();

            double resultDOGE = sum / priceDOGE;
            double resultETH = sum / priceETH;
            double resultBTC = sum / priceBTC;

            sendMassage(chatId, "doge - " + resultDOGE + "\n"
                    + "eth - " + resultETH + "\n"
                    + "btc - " + resultBTC);
            }else if (text.startsWith("btc") && isNumeric(text.substring(3))){
                double sum = Double.parseDouble(text.substring(3));
                var priceBTC = CryptoPrice.spotPrice("BTC").getAmount().doubleValue();
                double resultBTC = sum / priceBTC;
                sendMassage(chatId,"btc - " + resultBTC);
            } else {
            massage.setText("unknown command");
            }

        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    void sendMassage(long chatId, String text) throws Exception {
        var massage = new SendMessage();
        massage.setChatId(chatId);
        massage.setText(text);
        execute(massage);
    }

    void sendPicture(long chatId, String name) throws Exception {
        var photo = getClass().getClassLoader().getResourceAsStream(name);
        var massage = new SendPhoto();
        massage.setChatId(chatId);
        massage.setPhoto(new InputFile(photo, name));
        execute(massage);
    }

    void sendPrice(long chatId, String name) throws Exception {
        var price = CryptoPrice.spotPrice(name);
        sendMassage(chatId,name + "price: " + price.getAmount().doubleValue());
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String getBotUsername() {
        return "it_start_practice_bot";
    }
}
