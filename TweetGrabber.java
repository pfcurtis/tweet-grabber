import java.io.IOException;
import java.util.List;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import twitter4j.FilterQuery;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;


public class TweetGrabber {

    private class TweetStreamer implements StatusListener {
        private PrintStream ps;

        TweetStreamer() {
            try {
                ps = new PrintStream(System.out, true, "UTF-8");
            } catch (UnsupportedEncodingException ue) {
                    ue.printStackTrace();
            }
        }

        public void onStatus(Status s) {
            try {
                if (s.isRetweet()) {
                    // ps.print("Retweet ");
		    return;
                }
                ps.print(s.getUser().getScreenName());
                
                if (s.getPlace() != null) {
                   ps.print("  [" + s.getPlace().getFullName() + "]  ");
                }
                if (s.getGeoLocation() != null) {
                   ps.print(s.getGeoLocation().getLatitude() + " ");
                   ps.print(s.getGeoLocation().getLongitude());
                }
                ps.println("");
                ps.println(s.getText()+"\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        }

        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        }

        public void onScrubGeo(long userId, long upToStatusId) {
        }

        public void onStallWarning(StallWarning warning) {
        }

        public void onException(Exception ex) {
            ex.printStackTrace();
        }

    }

    public void stream() throws IOException {
        StatusListener listener = new TweetStreamer();

        TwitterStream ts = new TwitterStreamFactory().getInstance();
        ts.addListener(listener);

        FilterQuery fq = new FilterQuery();
//      fq.track(new String[]{"@nhregister","@nyscanner","@wtnh","@news12ct","@greenwichtime","@greenwichpost","@News12CT_Desk","@hartfordcourant"});
        fq.track(new String[]{"#hadoop","@mapr","#bigdata"});

        ts.filter(fq);
    }

    public static void main(String[] args) throws IOException, TwitterException {
       TweetGrabber t = new TweetGrabber();
       t.stream();
    }

}
