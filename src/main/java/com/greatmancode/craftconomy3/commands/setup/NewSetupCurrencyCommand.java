/**
 * This file is part of Craftconomy3.
 * <p>
 * Copyright (c) 2011-2016, Greatman <http://github.com/greatman/>
 * Copyright (c) 2016-2017, Aztorius <http://github.com/Aztorius/>
 * Copyright (c) 2018, Pavog <http://github.com/pavog/>
 * <p>
 * Craftconomy3 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Craftconomy3 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with Craftconomy3.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.greatmancode.craftconomy3.commands.setup;

import com.greatmancode.craftconomy3.Common;
import com.greatmancode.craftconomy3.NewSetupWizard;
import com.greatmancode.craftconomy3.commands.AbstractCommand;
import com.greatmancode.craftconomy3.currency.Currency;
import com.greatmancode.tools.commands.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class NewSetupCurrencyCommand extends AbstractCommand {

    private Map<String, String> map = new HashMap<>();

    public NewSetupCurrencyCommand(String name) {
        super(name);
    }

    private enum INTERNALSTEP {
        NAME,
        NAMEPLURAL,
        MINOR,
        MINORPLURAL,
        SIGN
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        // TODO: Check if the user has really given 2 args
        // And display a helpful message if not

        try {
            INTERNALSTEP step = INTERNALSTEP.valueOf(args[0].toUpperCase());

            if (step.equals(INTERNALSTEP.NAME)) {
                name(sender, args[1]);
            } else if (step.equals(INTERNALSTEP.NAMEPLURAL)) {
                namePlural(sender, args[1]);
            } else if (step.equals(INTERNALSTEP.MINOR)) {
                minor(sender, args[1]);
            } else if (step.equals(INTERNALSTEP.MINORPLURAL)) {
                minorPlural(sender, args[1]);
            } else if (step.equals(INTERNALSTEP.SIGN)) {
                sign(sender, args[1]);
            }
        } catch (IllegalArgumentException e) {
            sendMessage(sender, "{{DARK_RED}}Invalid sub-step! Please write a valid one.");
        }
    }

    @Override
    public String help() {
        return "/ccsetup currency - Configure the first currency";
    }

    @Override
    public int maxArgs() {
        return 2;
    }

    @Override
    public int minArgs() {
        return 2;
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermissionNode() {
        return "craftconomy.setup";
    }

    private void name(CommandSender sender, String name) {
        map.put("name", name);
        sendMessage(sender, "{{DARK_GREEN}}Now, let's configure the currency name but in {{WHITE}}Plural {{DARK_GREEN}}(Ex: {{WHITE}}Dollars{{DARK_GREEN}}). Please type {{WHITE}}/ccsetup currency nameplural <Plural>");
        done(sender);
    }

    private void namePlural(CommandSender sender, String namePlural) {
        map.put("nameplural", namePlural);
        sendMessage(sender, "{{DARK_GREEN}}Now, let's configure the currency name but for the {{WHITE}}minor {{DARK_GREEN}}(Ex: {{WHITE}}Coin{{DARK_GREEN}}). Please type {{WHITE}}/ccsetup currency minor <Minor>");
        done(sender);
    }

    private void minor(CommandSender sender, String minor) {
        map.put("minor", minor);
        sendMessage(sender, "{{DARK_GREEN}}Now, let's configure the currency name but for the {{WHITE}}minor in plural {{DARK_GREEN}}(Ex: {{WHITE}}Coins{{DARK_GREEN}}). Please type {{WHITE}}/ccsetup currency minorplural <Minor plural>");
        done(sender);
    }

    private void minorPlural(CommandSender sender, String minorPlural) {
        map.put("minorplural", minorPlural);
        Common.getInstance().getServerCaller().getPlayerCaller().sendMessage(sender.getUuid(), "{{DARK_GREEN}}Finally, let's put a {{WHITE}}sign {{DARK_GREEN}}on that currency (Ex: {{WHITE}}$ {{DARK_GREEN}}). Please type {{WHITE}}/ccsetup currency sign <Sign>");
        done(sender);
    }

    private void sign(CommandSender sender, String sign) {
        map.put("sign", sign);
        done(sender);
    }

    private void done(CommandSender sender) {
        if (map.size() == 5) {
            Currency currency = Common.getInstance().getCurrencyManager().addCurrency(map.get("name"), map.get("nameplural"), map.get("minor"), map.get("minorplural"), map.get("sign"), true);
            Common.getInstance().getCurrencyManager().setDefault(currency);
            Common.getInstance().getCurrencyManager().setDefaultBankCurrency(currency);
            sendMessage(sender, "{{DARK_GREEN}}We are done for that step! Only 2 remaining! Please type {{WHITE}}/ccsetup basic");
            NewSetupWizard.setState(NewSetupWizard.BASIC_STEP);
        }
    }
}
