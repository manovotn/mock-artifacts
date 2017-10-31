package client;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.wildfly.naming.client.WildFlyInitialContextFactory;
import org.xnio.XnioWorker;

import ejb.HelloBeanRemote;

public class Client {

    public static void main(String[] args)
            throws Exception {
        
        System.setProperty("wildfly.config.url", ClassLoader.getSystemResource(
            "client/test.xml").toString());
        InitialContext ctx = new InitialContext(getCtxProperties());
        if (XnioWorker.getContextManager().getGlobalDefault().getIoThreadCount() == 99) {
            // this is set in test.xml and SHOULD THROW EXCEPTION
            throw new IllegalStateException();
        }
        try {
            String lookupName = "ejb:/server/HelloBean!ejb.HelloBeanRemote?stateful";
            HelloBeanRemote bean = (HelloBeanRemote)ctx.lookup(lookupName);
            System.out.println(bean.hello());
        } finally {
            ctx.close();
        }
    }

    public static Properties getCtxProperties() {
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, WildFlyInitialContextFactory.class.getName());
        return props;
    }

}
