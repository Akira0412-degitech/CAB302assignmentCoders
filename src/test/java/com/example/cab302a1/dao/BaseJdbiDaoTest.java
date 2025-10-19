package com.example.cab302a1.dao;

import com.example.cab302a1.DBconnection;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.statement.Query;
import org.jdbi.v3.core.statement.Update;
import org.mockito.MockedStatic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * ✅ Base class for all JDBI DAO tests.
 *
 * Provides shared Mockito setup for mocking JDBI behavior,
 * so DAO logic can be tested without a real database.
 */
public abstract class BaseJdbiDaoTest {

    protected Jdbi mockJdbi;
    protected Handle mockHandle;
    protected Update mockUpdate;
    protected Query mockQuery;
    protected MockedStatic<DBconnection> dbMock;

    @BeforeEach
    protected void setupCommonMocks() {
        if (dbMock != null) {
            try { dbMock.close(); } catch (Exception ignored) {}
        }

        mockJdbi = mock(Jdbi.class);
        mockHandle = mock(Handle.class);
        mockUpdate = mock(Update.class);
        mockQuery = mock(Query.class);

        dbMock = mockStatic(DBconnection.class);
        dbMock.when(DBconnection::getJdbi).thenReturn(mockJdbi);

        // ✅ Mock Jdbi.useHandle() — executes consumer with mock handle
        doAnswer(invocation -> {
            HandleConsumer<?> consumer = invocation.getArgument(0);
            consumer.useHandle(mockHandle);
            return null;
        }).when(mockJdbi).useHandle(any());

        // ✅ Mock Jdbi.withHandle() — executes callback with mock handle
        when(mockJdbi.withHandle(any())).thenAnswer(invocation -> {
            HandleCallback<Object, Exception> callback = invocation.getArgument(0);
            return callback.withHandle(mockHandle);
        });

        // ✅ Basic behavior for handle
        when(mockHandle.createUpdate(anyString())).thenReturn(mockUpdate);
        when(mockHandle.createQuery(anyString())).thenReturn(mockQuery);

        // ✅ Allow chaining for all primitive and object types (Update)

        doAnswer(invocation -> mockUpdate).when(mockUpdate).bind(anyString(), anyInt());
        doAnswer(invocation -> mockUpdate).when(mockUpdate).bind(anyString(), anyBoolean());
        doAnswer(invocation -> mockUpdate).when(mockUpdate).bind(anyString(), anyLong());
        doAnswer(invocation -> mockUpdate).when(mockUpdate).bind(anyString(), anyDouble());

        // ✅ Allow chaining for all primitive and object types (Query)

        doAnswer(invocation -> mockQuery).when(mockQuery).bind(anyString(), anyInt());
        doAnswer(invocation -> mockQuery).when(mockQuery).bind(anyString(), anyBoolean());
        doAnswer(invocation -> mockQuery).when(mockQuery).bind(anyString(), anyLong());
        doAnswer(invocation -> mockQuery).when(mockQuery).bind(anyString(), anyDouble());

    }

    @AfterEach
    void tearDown() {
        if (dbMock != null) dbMock.close();
    }
}
