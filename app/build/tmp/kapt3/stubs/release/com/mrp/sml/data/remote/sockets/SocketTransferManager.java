package com.mrp.sml.data.remote.sockets;

import com.mrp.sml.core.constants.TransferConstants;
import com.mrp.sml.core.models.TransferProgress;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.flow.StateFlow;
import timber.log.Timber;
import java.io.File;
import java.security.MessageDigest;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000t\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\b\u0007\u0018\u0000 <2\u00020\u0001:\u0001<B\u0007\b\u0007\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0017\u001a\u00020\u0018J\u000e\u0010\u0019\u001a\u00020\u000b2\u0006\u0010\u001a\u001a\u00020\u001bJ\u0018\u0010\u001c\u001a\u00020\u00142\u0006\u0010\u001d\u001a\u00020\u00142\u0006\u0010\u001e\u001a\u00020\u0014H\u0002J\u0018\u0010\u001f\u001a\u00020\u00142\u0006\u0010 \u001a\u00020\u00142\u0006\u0010\u001e\u001a\u00020\u0014H\u0002J\u0006\u0010!\u001a\u00020\u0005J\u0006\u0010\"\u001a\u00020\u000bJ\u0006\u0010#\u001a\u00020\u0018JH\u0010$\u001a\b\u0012\u0004\u0012\u00020\u001b0%2\u0006\u0010&\u001a\u00020\'2\u0006\u0010(\u001a\u00020\u001b2\u0006\u0010)\u001a\u00020*2\b\b\u0002\u0010+\u001a\u00020*2\b\b\u0002\u0010,\u001a\u00020*H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b-\u0010.J\u0006\u0010/\u001a\u00020\u0018J\u0006\u00100\u001a\u00020\u0018J\u0010\u00101\u001a\u00020\u000b2\u0006\u00102\u001a\u00020\u000bH\u0002JP\u00103\u001a\b\u0012\u0004\u0012\u00020\u00180%2\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u00104\u001a\u0002052\u0006\u0010)\u001a\u00020*2\u0006\u0010+\u001a\u00020*2\b\b\u0002\u0010,\u001a\u00020*2\b\b\u0002\u00106\u001a\u000207H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b8\u00109J\u000e\u0010:\u001a\u00020\u00182\u0006\u0010;\u001a\u00020\u000bR\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00070\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00050\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00070\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0010\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006="}, d2 = {"Lcom/mrp/sml/data/remote/sockets/SocketTransferManager;", "", "()V", "_progress", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/mrp/sml/core/models/TransferProgress;", "_state", "Lcom/mrp/sml/data/remote/sockets/TransferState;", "cancelled", "", "currentSessionToken", "", "paused", "progress", "Lkotlinx/coroutines/flow/StateFlow;", "getProgress", "()Lkotlinx/coroutines/flow/StateFlow;", "secureRandom", "Ljava/security/SecureRandom;", "sessionKey", "", "state", "getState", "cancel", "", "computeSha256", "file", "Ljava/io/File;", "decryptChunk", "encrypted", "nonce", "encryptChunk", "data", "getCurrentProgress", "getCurrentSessionToken", "pause", "receiveFile", "Lkotlin/Result;", "input", "Ljava/io/DataInputStream;", "outputDir", "fileIndex", "", "totalFiles", "startChunk", "receiveFile-hUnOzRk", "(Ljava/io/DataInputStream;Ljava/io/File;IIILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "reset", "resume", "sanitizeFileName", "name", "sendFile", "output", "Ljava/io/DataOutputStream;", "startTransferred", "", "sendFile-bMdYcbs", "(Ljava/io/File;Ljava/io/DataOutputStream;IIIJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setSessionToken", "token", "Companion", "app_release"})
public final class SocketTransferManager {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.mrp.sml.core.models.TransferProgress> _progress = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.mrp.sml.core.models.TransferProgress> progress = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.mrp.sml.data.remote.sockets.TransferState> _state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.mrp.sml.data.remote.sockets.TransferState> state = null;
    @org.jetbrains.annotations.NotNull()
    private final java.security.SecureRandom secureRandom = null;
    @org.jetbrains.annotations.Nullable()
    private byte[] sessionKey;
    private boolean cancelled = false;
    private boolean paused = false;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String currentSessionToken = "";
    public static final byte TYPE_FILE_START = (byte)4;
    public static final byte TYPE_CHUNK = (byte)5;
    public static final byte TYPE_FILE_DONE = (byte)7;
    @org.jetbrains.annotations.NotNull()
    public static final com.mrp.sml.data.remote.sockets.SocketTransferManager.Companion Companion = null;
    
    @javax.inject.Inject()
    public SocketTransferManager() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.mrp.sml.core.models.TransferProgress> getProgress() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.mrp.sml.data.remote.sockets.TransferState> getState() {
        return null;
    }
    
    public final void setSessionToken(@org.jetbrains.annotations.NotNull()
    java.lang.String token) {
    }
    
    public final void cancel() {
    }
    
    public final void pause() {
    }
    
    public final void resume() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCurrentSessionToken() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.mrp.sml.core.models.TransferProgress getCurrentProgress() {
        return null;
    }
    
    public final void reset() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String computeSha256(@org.jetbrains.annotations.NotNull()
    java.io.File file) {
        return null;
    }
    
    private final byte[] encryptChunk(byte[] data, byte[] nonce) {
        return null;
    }
    
    private final byte[] decryptChunk(byte[] encrypted, byte[] nonce) {
        return null;
    }
    
    private final java.lang.String sanitizeFileName(java.lang.String name) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0005\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/mrp/sml/data/remote/sockets/SocketTransferManager$Companion;", "", "()V", "TYPE_CHUNK", "", "TYPE_FILE_DONE", "TYPE_FILE_START", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}