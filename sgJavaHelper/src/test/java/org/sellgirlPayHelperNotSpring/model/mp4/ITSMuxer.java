package org.sellgirlPayHelperNotSpring.model.mp4;

//import android.media.MediaCodec;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface ITSMuxer {
//    void writeFrame(ByteBuffer buffer, MediaCodec.BufferInfo info) throws IOException;
    void writeFrame(byte[] data, MediaCodec.BufferInfo info) throws IOException;
//	void writeFrame(ByteBuffer buffer,byte[] data) throws IOException;
     void close() throws IOException;
}
