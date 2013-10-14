package com.muframe.server.photofilters;

import java.io.File;
import java.io.IOException;

/**
 * Photo filters are applied in the requested order
 * FIXME specify order to execute filters
 * @author mario
 *
 */
public interface PhotoFilter {
	public void filter(File photo, File output) throws IOException;
}
