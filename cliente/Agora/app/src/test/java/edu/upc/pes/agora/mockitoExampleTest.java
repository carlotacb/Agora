package edu.upc.pes.agora;

/**
 * Created by gerar on 20/10/2017.
 */

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import android.content.Context;
import android.content.SharedPreferences;
import static org.junit.Assert.*;



@RunWith(MockitoJUnitRunner.class)
public class mockitoExampleTest {
    @Mock
    Context mMockContext;


    @Test
    public void mockitoOk() {
        HttpHelper mockWarehouse = mock(HttpHelper.class);

         when(mockWarehouse.verifyData("","","",""))
                .thenReturn(true);

        Boolean result = mockWarehouse.verifyData("","","","");

        assertTrue(result);

    }

    @Test
    public void mockitoFalse() {
        HttpHelper mockWarehouse = mock(HttpHelper.class);

        when(mockWarehouse.verifyData("code","","",""))
                .thenReturn(true);

        Boolean result = mockWarehouse.verifyData("","","","");

        assertFalse(result);
    }

    @Test
    public void mockitoAll() {
        HttpHelper mockWarehouse = mock(HttpHelper.class);

        when(mockWarehouse.verifyData(anyString(),anyString(),anyString(),anyString()))
                .thenReturn(true);

        Boolean result = mockWarehouse.verifyData("code","user","pass","pass2");

        assertTrue(result);

    }
}
