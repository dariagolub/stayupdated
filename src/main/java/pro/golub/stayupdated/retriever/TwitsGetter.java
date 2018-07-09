package pro.golub.stayupdated.retriever;

import akka.actor.AbstractActor;
import akka.actor.Props;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.time.LocalDate;

public class TwitsGetter extends AbstractActor {

    static public Props props(Twitter twitter, JavaSparkContext sparkContext) {
        return Props.create(TwitsGetter.class, () -> new TwitsGetter(twitter, sparkContext));
    }

    private final Twitter twitter;
    private final JavaSparkContext sparkContext;

    public TwitsGetter(Twitter twitter, JavaSparkContext sparkContext) {
        this.twitter = twitter;
        this.sparkContext = sparkContext;
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, this::processQuery)
                .build();
    }

    private void processQuery(String hashTag) {
        QueryResult result;
        try {
            result = twitter.search(QueryBuilder.buildQuery(hashTag));
            for (Status status : result.getTweets()) {
                System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
                System.out.println("Date: " + status.getCreatedAt().toString() + ", likes: " + status.getFavoriteCount()
                        + ", retweets: " + status.getRetweetCount());
            }

            JavaRDD<Status> javaRDD = sparkContext.parallelize(result.getTweets());
            javaRDD.saveAsObjectFile("result/twitter_" + hashTag + "_" + LocalDate.now().minusDays(1).toString());
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}
