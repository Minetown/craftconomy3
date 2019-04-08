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
package com.greatmancode.craftconomy3.commands.currency;

import com.greatmancode.craftconomy3.Common;
import com.greatmancode.craftconomy3.commands.AbstractCommand;
import com.greatmancode.tools.commands.CommandSender;

public class CurrencyAddCommand extends AbstractCommand {

    public CurrencyAddCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 4 && args[0].length() > 0 && args[1].length() > 0 && args[2].length() > 0 && args[3].length() > 0 && args[4].length() > 0) {
            if (Common.getInstance().getCurrencyManager().getCurrency(args[0]) == null) {
                Common.getInstance().getCurrencyManager().addCurrency(args[0], args[1], args[2], args[3], args[4], true);
                sendMessage(sender, Common.getInstance().getLanguageManager().getString("currency_added"));
            } else {
                sendMessage(sender, Common.getInstance().getLanguageManager().getString("currency_already_exists"));
            }
        }
    }

    @Override
    public String help() {
        return Common.getInstance().getLanguageManager().getString("currency_add_cmd_help");
    }

    @Override
    public int maxArgs() {
        return 5;
    }

    @Override
    public int minArgs() {
        return 5;
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermissionNode() {
        return "craftconomy.currency.add";
    }
}
