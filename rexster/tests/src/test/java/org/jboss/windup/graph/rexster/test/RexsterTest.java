package org.jboss.windup.graph.rexster.test;

import java.io.IOException;
import java.net.Socket;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.arquillian.AddonDependency;
import org.jboss.forge.arquillian.AddonDependencies;
import org.jboss.forge.arquillian.archive.AddonArchive;
import org.jboss.forge.furnace.Furnace;
import org.jboss.forge.furnace.services.Imported;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.windup.config.furnace.FurnaceHolder;
import org.jboss.windup.graph.GraphContext;
import org.jboss.windup.graph.GraphContextFactory;
import org.jboss.windup.graph.listeners.AfterGraphInitializationListener;
import org.jboss.windup.graph.rexster.RexsterInitializer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * A test for the rexster initialization
 * 
 * @author <a href="mailto:mbriskar@gmail.com">Matej Briskar</a>
 *
 */
@RunWith(Arquillian.class)
public class RexsterTest
{
    @Deployment
    @AddonDependencies({
                @AddonDependency(name = "org.jboss.windup.config:windup-config"),
                @AddonDependency(name = "org.jboss.windup.graph:windup-graph"),
                @AddonDependency(name = "org.jboss.windup.rexster:windup-rexster"),
                @AddonDependency(name = "org.jboss.forge.furnace.container:simple")
    })
    public static AddonArchive getDeployment()
    {
        final AddonArchive archive = ShrinkWrap.create(AddonArchive.class)
                    .addAsLocalServices(RexsterTest.class);

        return archive;
    }

    @Test
    public void testRexsterProperStart() throws IOException, InstantiationException, IllegalAccessException
    {
        Furnace furnace = FurnaceHolder.getFurnace();
        Imported<GraphContextFactory> factory = furnace.getAddonRegistry().getServices(GraphContextFactory.class);
        try (GraphContext context = factory.get().create())
        {
            Imported<AfterGraphInitializationListener> afterInitializationListeners = furnace.getAddonRegistry().getServices(
                        AfterGraphInitializationListener.class);
            boolean rexsterFound = false;
            for (AfterGraphInitializationListener listener : afterInitializationListeners)
            {
                if (listener instanceof RexsterInitializer)
                {
                    rexsterFound = true;
                }
            }
            if (!rexsterFound)
            {
                Assert.fail("Rexster was not found among registered listeners");
            }

            Socket s = null;
            try
            {
                s = new Socket("localhost", 8182);
            }
            catch (Exception e)
            {
                Assert.fail("Rexster is not listening on localhost:8182");
            }
            finally
            {
                if (s != null)
                    try
                    {
                        s.close();
                    }
                    catch (Exception e)
                    {
                    }
            }
        }
    }
}
