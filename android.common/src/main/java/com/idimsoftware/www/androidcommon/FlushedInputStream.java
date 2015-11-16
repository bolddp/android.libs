package com.idimsoftware.www.androidcommon;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

// Class that provides workaround for image download on slow connection
// when the entire stream isn't delivered at once.
// Fetched from http://code.google.com/p/android/issues/detail?id=6066
public class FlushedInputStream extends FilterInputStream {
	public FlushedInputStream(InputStream inputStream) {
		super(inputStream);
	}

	@Override
	public long skip(long n) throws IOException {
		long totalBytesSkipped = 0L;
		while (totalBytesSkipped < n) {
			long bytesSkipped = in.skip(n - totalBytesSkipped);
			if (bytesSkipped == 0L) {
				int bte = read();
				if (bte < 0) {
					break; // we reached EOF
				} else {
					bytesSkipped = 1; // we read one byte
				}
			}
			totalBytesSkipped += bytesSkipped;
		}
		return totalBytesSkipped;
	}
}

