package ca.concordia.ginacody.comp6231.client.controller;

import ca.concordia.ginacody.comp6231.client.shell.ShellHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class GameController {

    /**
     * Class Logger
     */
    private static Logger log = LoggerFactory.getLogger(GameController.class);

    /**
     * shellHelper bean
     */
    @Autowired
    ShellHelper shellHelper;

    @ShellMethod("Save the current game state")
    public String login() {

        if (log.isDebugEnabled()) {
            log.debug("inside login");
        }
        return shellHelper.getSuccessMessage("Game saved successfully");
    }

}
