package com.dgsoft.common.ws;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Logging;
import org.richfaces.application.push.MessageException;
import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.TopicsContext;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by cooper on 10/17/15.
 */
@Name("extendsMessageService")
@Scope(ScopeType.STATELESS)
@WebService(name = "ExtendsMessageService",targetNamespace = "http://ws.system.common.dgsoft.com/")
@HandlerChain(file = "soap-handlers.xml")
public class ExtendsMessageService {



    @WebMethod()
    public boolean personCerInfo(@WebParam(name = "key" ,targetNamespace = "http://ws.system.common.dgsoft.com/")String key, @WebParam(name = "cer", targetNamespace = "http://ws.system.common.dgsoft.com/")String cer){
        Logging.getLog("recive data:" + cer);
        TopicKey topicKey = new TopicKey(key);
        TopicsContext topicsContext = TopicsContext.lookup();
        try {
            topicsContext.publish(topicKey, cer);
           // topicsContext.removeTopic(topicKey);

            return true;

        } catch (MessageException e) {
            topicsContext.removeTopic(topicKey);
            Logging.getLog(getClass()).debug(e.getMessage(),e);
            return false;
        }
    }
}
