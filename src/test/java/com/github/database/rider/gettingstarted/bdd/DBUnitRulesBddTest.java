package com.github.database.rider.gettingstarted.bdd;

import com.github.database.rider.cucumber.CdiCucumberTestRunner;
import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Created by rmpestano on 4/17/16.
 */
@RunWith(CdiCucumberTestRunner.class)
@CucumberOptions(features ="src/test/resources/features/search-users.feature")
public class DBUnitRulesBddTest {
}
