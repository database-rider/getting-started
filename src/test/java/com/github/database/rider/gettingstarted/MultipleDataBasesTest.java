package com.github.database.rider.gettingstarted;

import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.DataSetExecutor;
import com.github.database.rider.core.configuration.DataSetConfig;
import com.github.database.rider.core.connection.ConnectionHolderImpl;
import com.github.database.rider.core.dataset.DataSetExecutorImpl;
import com.github.database.rider.core.util.EntityManagerProvider;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by pestano on 23/07/15.
 */

@RunWith(JUnit4.class)
public class MultipleDataBasesTest {

    @Rule
    public EntityManagerProvider emProvider = EntityManagerProvider.instance("pu1");

    @Rule
    public EntityManagerProvider emProvider2 = EntityManagerProvider.instance("pu2");

    @Rule
    public DBUnitRule rule1 = DBUnitRule.instance("rule1",emProvider.connection()); //<1>

    @Rule
    public DBUnitRule rule2 = DBUnitRule.instance("rule2",emProvider2.connection());


    @Test
    @DataSet(value = "users.yml", executorId = "rule1") //<2>
    public void shouldSeedDatabaseUsingPu1() {
        User user = (User) emProvider.em().
                createQuery("select u from User u where u.id = 1").getSingleResult();
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
    }

    @Test
    @DataSet(value = "users.yml", executorId = "rule2")
    public void shouldSeedDatabaseUsingPu2() {
        User user = (User) emProvider2.em().
                createQuery("select u from User u where u.id = 1").getSingleResult();
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
    }

    @Test //<3>
    public void shouldSeedDatabaseUsingMultiplePus() {
        DataSetExecutor exec1 = DataSetExecutorImpl.
                instance("exec1", new ConnectionHolderImpl(emProvider.connection()));
        DataSetExecutor exec2 = DataSetExecutorImpl.
                instance("exec2", new ConnectionHolderImpl(emProvider2.connection()));

        //programmatic seed db1
        exec1.createDataSet(new DataSetConfig("users.yml"));

        exec2.createDataSet(new DataSetConfig("dataset-with-javascript.yml"));//seed db2

        //user comes from database represented by pu1
        User user = (User) emProvider.em().
                createQuery("select u from User u where u.id = 1").getSingleResult();
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);

        //tweets comes from pu2
        Tweet tweet = (Tweet) emProvider.em().createQuery("select t from Tweet t where t.id = 1").getSingleResult();
        assertThat(tweet).isNotNull();
        assertThat(tweet.getLikes()).isEqualTo(50);
    }

}
