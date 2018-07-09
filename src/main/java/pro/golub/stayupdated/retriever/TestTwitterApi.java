package pro.golub.stayupdated.retriever;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import scala.concurrent.duration.FiniteDuration;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by daryagolub on 18/12/17.
 */
public class TestTwitterApi {

    private static final String CONSUMER_KEY = "PcREQWNILammbCWZYbYfAr5Ch";
    private static final String CONSUMER_SECRET = "9iq21i3JEZr9fNdJjzkYmGqu6WeusIHzOfM2cpfAnEqlkpCgbG";
    private static final String TOKEN = "188279042-TeSVv7klfq5CGRwGruxUSdYhK0lltHKn7YijVycW";
    private static final String TOKEN_SECRET = "jEdcuH28lx34RzgcaTMw9of5FJ7HzDYOVYvJ5octmQoBY";

    private static Set<String> hashTags = new HashSet<>(Arrays.asList("java", "blockchain", "infosec"));

    public static void main(String args[]) {

        // The factory instance is re-usable and thread safe.
        TwitterFactory factory = new TwitterFactory();
        AccessToken accessToken = loadAccessToken();
        Twitter twitter = factory.getInstance();
        twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        twitter.setOAuthAccessToken(accessToken);

        SparkConf conf = new SparkConf().setAppName("stayupdated").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

        final ActorSystem actorSystem = ActorSystem.create("twitGet");


        final ActorRef twitGetter = actorSystem.actorOf(TwitsGetter.props(twitter, sc));

        hashTags.forEach(hashTag -> {
            actorSystem.scheduler().schedule(FiniteDuration.apply(calculateDelay(), TimeUnit.MILLISECONDS)
                    , FiniteDuration.create(24, TimeUnit.HOURS)
                    , twitGetter, hashTag, actorSystem.dispatcher(), ActorRef.noSender());
            //TODO is it correct way to work with actors?
        });


    }

    private static long calculateDelay() {
        return LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(6, 0)).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - System.currentTimeMillis();
        //return LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 7)).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - System.currentTimeMillis();
    }

    private static AccessToken loadAccessToken() {
        return new AccessToken(TOKEN, TOKEN_SECRET);
    }
}
