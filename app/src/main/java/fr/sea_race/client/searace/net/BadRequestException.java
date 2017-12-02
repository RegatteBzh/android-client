package fr.sea_race.client.searace.net;

/**
 * Created by cyrille on 02/12/17.
 */

public class BadRequestException extends Exception {
    public BadRequestException(int statusCode) {
        super("Bad Request: " + statusCode);
    }
}
