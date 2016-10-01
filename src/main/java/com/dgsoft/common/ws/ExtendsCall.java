package com.dgsoft.common.ws;

import org.jboss.seam.log.Logging;
import org.richfaces.application.push.MessageException;
import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.TopicsContext;

import javax.ws.rs.*;

/**
 * Created by cooper on 9/26/16.
 */
@Path("/extends")
public class ExtendsCall {

    @POST
    @Path("/person/{key}")
    public String putPerson(@PathParam("key") String key , @FormParam("cer") String cer){

        Logging.getLog("recive data:" + cer);
        TopicKey topicKey = new TopicKey(key);
        TopicsContext topicsContext = TopicsContext.lookup();
        try {
            topicsContext.publish(topicKey, cer);
            // topicsContext.removeTopic(topicKey);

            return "success";

        } catch (MessageException e) {
            topicsContext.removeTopic(topicKey);
            Logging.getLog(getClass()).debug(e.getMessage(),e);
            return "failed";
        }


    }


    @POST
    @Path("/fingerprint/{key}")
    public String putFingerprint(@PathParam("key") String key, @FormParam("code") String code, @FormParam("pic") String pic){





        return "success";
    }

}
