package com.muframe.server.photofilters;

import java.io.File;
import java.io.IOException;

/**
 * Photo filters are applied in the requested order
 * FIXME specify order to execute filters
 * @author danirigolin
 *
 */
public interface PhotoFilter {
	public File filter(File photo) throws IOException;
}
