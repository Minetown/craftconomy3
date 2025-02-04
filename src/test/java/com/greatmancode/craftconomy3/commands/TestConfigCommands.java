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
package com.greatmancode.craftconomy3.commands;

import com.greatmancode.craftconomy3.Common;
import com.greatmancode.craftconomy3.DisplayFormat;
import com.greatmancode.craftconomy3.TestCommandSender;
import com.greatmancode.craftconomy3.TestInitializator;
import com.greatmancode.craftconomy3.commands.config.*;
import com.greatmancode.tools.commands.PlayerCommandSender;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class TestConfigCommands {

    private static final String TEST_ACCOUNT = "testuser30";
    private static final String TEST_ACCOUNT2 = "Testuser31";
    private static final String TEST_ACCOUNT3 = "Testuser32";
    private static final String TEST_ACCOUNT4 = "Testuser34";
    private static PlayerCommandSender TEST_USER;

    @Before
    public void setUp() {
        new TestInitializator();
        TEST_USER = createTestUser("TestUser39");
    }

    @After
    public void close() {
        Common.getInstance().onDisable();
    }

    @Test
    public void testClearLogCommand() {
        ConfigClearLogCommand command = new ConfigClearLogCommand("clearlog");
        command.execute(TEST_USER, new String[]{"1"});

        // Test with invalid argument
        command.execute(TEST_USER, new String[]{"a"});
    }

    @Test
    public void testReloadCommand() {
        ConfigReloadCommand command = new ConfigReloadCommand("reload");
        command.execute(TEST_USER, new String[]{});
    }

    @Test
    public void testBankPriceCommand() {
        ConfigBankPriceCommand command = new ConfigBankPriceCommand("price");
        command.execute(TEST_USER, new String[]{"200"});
        assertEquals(200, Common.getInstance().getBankPrice(), 0);
        command.execute(TEST_USER, new String[]{"-10"});
        assertEquals(200, Common.getInstance().getBankPrice(), 0);
        command.execute(TEST_USER, new String[]{"adjbf"});
        assertEquals(200, Common.getInstance().getBankPrice(), 0);
        command.execute(TEST_USER, new String[]{"0"});
        assertEquals(0, Common.getInstance().getBankPrice(), 0);
    }

    @Test
    public void testFormatCommand() {
        ConfigFormatCommand command = new ConfigFormatCommand("format");
        command.execute(TEST_USER, new String[]{"long"});
        assertEquals(DisplayFormat.LONG, Common.getInstance().getDisplayFormat());
        command.execute(TEST_USER, new String[]{"sign"});
        assertEquals(DisplayFormat.SIGN, Common.getInstance().getDisplayFormat());
        command.execute(TEST_USER, new String[]{"signfront"});
        assertEquals(DisplayFormat.SIGNFRONT, Common.getInstance().getDisplayFormat());
        command.execute(TEST_USER, new String[]{"majoronly"});
        assertEquals(DisplayFormat.MAJORONLY, Common.getInstance().getDisplayFormat());
        command.execute(TEST_USER, new String[]{"small"});
        assertEquals(DisplayFormat.SMALL, Common.getInstance().getDisplayFormat());
        command.execute(TEST_USER, new String[]{"0ewhf"});
        assertEquals(DisplayFormat.SMALL, Common.getInstance().getDisplayFormat());
    }

    @Test
    public void testHoldingsCommand() {
        ConfigHoldingsCommand command = new ConfigHoldingsCommand(null);

        command.execute(TEST_USER, new String[]{"200"});
        assertEquals(200, Common.getInstance().getDefaultHoldings(), 0);
        Common.getInstance().getAccountManager().getAccount(TEST_ACCOUNT3, false);
        assertEquals(200, Common.getInstance().getAccountManager().getAccount(TEST_ACCOUNT3, false).getBalance("default", Common.getInstance().getCurrencyManager().getDefaultCurrency().getName()), 0);

        command.execute(TEST_USER, new String[]{"-10"});
        assertEquals(200, Common.getInstance().getDefaultHoldings(), 0);
        Common.getInstance().getAccountManager().getAccount(TEST_ACCOUNT4, false);
        assertEquals(200, Common.getInstance().getAccountManager().getAccount(TEST_ACCOUNT4, false).getBalance("default", Common.getInstance().getCurrencyManager().getDefaultCurrency().getName()), 0);

        command.execute(TEST_USER, new String[]{"adjbf"});
        assertEquals(200, Common.getInstance().getDefaultHoldings(), 0);
        Common.getInstance().getAccountManager().getAccount(TEST_ACCOUNT2, false);
        assertEquals(200, Common.getInstance().getAccountManager().getAccount(TEST_ACCOUNT2, false).getBalance("default", Common.getInstance().getCurrencyManager().getDefaultCurrency().getName()), 0);

        command.execute(TEST_USER, new String[]{"0"});
        assertEquals(0, Common.getInstance().getDefaultHoldings(), 0);
        Common.getInstance().getAccountManager().getAccount(TEST_ACCOUNT, false);
        assertEquals(0, Common.getInstance().getAccountManager().getAccount(TEST_ACCOUNT, false).getBalance("default", Common.getInstance().getCurrencyManager().getDefaultCurrency().getName()), 0);
    }

    private PlayerCommandSender createTestUser(String name) {
        UUID test = UUID.randomUUID();
        return new PlayerCommandSender<>(name, test, new TestCommandSender(test, name));
    }
}
