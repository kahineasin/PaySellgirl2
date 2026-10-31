package org.sellgirlPayHelperNotSpring.model.mp4;

public class MediaCodec {
    private static final String TAG = "MediaCodec";

    /**
     * Per buffer metadata includes an offset and size specifying
     * the range of valid data in the associated codec (output) buffer.
     */
    public final static class BufferInfo {
//        /**
//         * Update the buffer metadata information.
//         *
//         * @param newOffset the start-offset of the data in the buffer.
//         * @param newSize   the amount of data (in bytes) in the buffer.
//         * @param newTimeUs the presentation timestamp in microseconds.
//         * @param newFlags  buffer flags associated with the buffer.  This
//         * should be a combination of  {@link #BUFFER_FLAG_KEY_FRAME} and
//         * {@link #BUFFER_FLAG_END_OF_STREAM}.
//         */
//        public void set(
//                int newOffset, int newSize, long newTimeUs, @BufferFlag int newFlags) {
//            offset = newOffset;
//            size = newSize;
//            presentationTimeUs = newTimeUs;
//            flags = newFlags;
//        }

        /**
         * The start-offset of the data in the buffer.
         */
        public int offset;

        /**
         * The amount of data (in bytes) in the buffer.  If this is {@code 0},
         * the buffer has no data in it and can be discarded.  The only
         * use of a 0-size buffer is to carry the end-of-stream marker.
         */
        public int size;

        /**
         * The presentation timestamp in microseconds for the buffer.
         * This is derived from the presentation timestamp passed in
         * with the corresponding input buffer.  This should be ignored for
         * a 0-sized buffer.
         */
        public long presentationTimeUs;

        /**
         * Buffer flags associated with the buffer.  A combination of
         * {@link #BUFFER_FLAG_KEY_FRAME} and {@link #BUFFER_FLAG_END_OF_STREAM}.
         *
         * <p>Encoded buffers that are key frames are marked with
         * {@link #BUFFER_FLAG_KEY_FRAME}.
         *
         * <p>The last output buffer corresponding to the input buffer
         * marked with {@link #BUFFER_FLAG_END_OF_STREAM} will also be marked
         * with {@link #BUFFER_FLAG_END_OF_STREAM}. In some cases this could
         * be an empty buffer, whose sole purpose is to carry the end-of-stream
         * marker.
         */
//        @BufferFlag
        public int flags;

//        /** @hide */
////        @NonNull
//        public BufferInfo dup() {
//            BufferInfo copy = new BufferInfo();
//            copy.set(offset, size, presentationTimeUs, flags);
//            return copy;
//        }
    };

    /**
     * This indicates that the (encoded) buffer marked as such contains
     * the data for a key frame.
     */
    public static final int BUFFER_FLAG_KEY_FRAME = 1;    
    /**
     * This signals the end of stream, i.e. no buffers will be available
     * after this, unless of course, {@link #flush} follows.
     */
    public static final int BUFFER_FLAG_END_OF_STREAM = 4;
}
