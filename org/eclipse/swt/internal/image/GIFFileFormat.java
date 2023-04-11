/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.image;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import java.io.*;

final class GIFFileFormat extends FileFormat {
	String signature, version;
	boolean sorted;
	int screenWidth, screenHeight, backgroundPixel, aspect, bitsPerPixel, defaultDepth;
	boolean userInput = false;
	int disposalMethod = 0;
	int delayTime = 0;
	int transparentPixel = -1;
	int repeatCount = 1;
	
	static final int GIF_APPLICATION_EXTENSION_BLOCK_ID = 0xFF;
	static final int GIF_GRAPHICS_CONTROL_BLOCK_ID = 0xF9;
	static final int GIF_PLAIN_TEXT_BLOCK_ID = 0x01;
	static final int GIF_COMMENT_BLOCK_ID = 0xFE;
	static final int GIF_EXTENSION_BLOCK_ID = 0x21;
	static final int GIF_IMAGE_BLOCK_ID = 0x2C;
	static final int GIF_TRAILER_ID = 0x3B;
	
	/**
	 * Answer a palette containing numGrays
	 * shades of gray, ranging from black to white.
	 */
	static PaletteData grayRamp(int numGrays) {
		int n = numGrays - 1;
		RGB[] colors = new RGB[numGrays];
		for (int i = 0; i < numGrays; i++) {
			int intensity = (byte)((i * 3) * 256 / n);
			colors[i] = new RGB(intensity, intensity, intensity);
		}
		return new PaletteData(colors);
	}

	boolean isFileFormat(LEDataInputStream stream) {
		try {
			byte[] signature = new byte[3];
			stream.read(signature);
			stream.unread(signature);
			return new String(signature).equals("GIF"); //$NON-NLS-1$
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Load the GIF image(s) stored in the input stream.
	 * Return an array of ImageData representing the image(s).
	 */
	ImageData[] loadFromByteStream() {
		byte[] signatureBytes = new byte[3];
		byte[] versionBytes = new byte[3];
		byte[] block = new byte[7];
		try {
			inputStream.read(signatureBytes);
			signature = new String(signatureBytes);
			if (!signature.equals("GIF")) //$NON-NLS-1$
				SWT.error(SWT.ERROR_INVALID_IMAGE);

			inputStream.read(versionBytes);
			version = new String(versionBytes);

			inputStream.read(block);
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
		}
		screenWidth = (block[0] & 0xFF) | ((block[1] & 0xFF) << 8);
		loader.logicalScreenWidth = screenWidth;
		screenHeight = (block[2] & 0xFF) | ((block[3] & 0xFF) << 8);
		loader.logicalScreenHeight = screenHeight;
		byte bitField = block[4];
		backgroundPixel = block[5] & 0xFF;
		aspect = block[6] & 0xFF;
		bitsPerPixel = ((bitField >> 4) & 0x07) + 1;
		defaultDepth = (bitField & 0x7) + 1;
		PaletteData palette = null;
		if ((bitField & 0x80) != 0) {
			// Global palette.
			sorted = (bitField & 0x8) != 0;
			palette = readPalette(1 << defaultDepth);
		} else {
			// No global palette.
			sorted = false;
			backgroundPixel = -1;
			defaultDepth = bitsPerPixel;
		}
		loader.backgroundPixel = backgroundPixel;

		getExtensions();
		int id = readID();
		ImageData[] images = new ImageData[0];
		while (id == GIF_IMAGE_BLOCK_ID) {
			ImageData image = readImageBlock(palette);
			if (loader.hasListeners()) {
				loader.notifyListeners(new ImageLoaderEvent(loader, image, 3, true));
			}
			ImageData[] oldImages = images;
			images = new ImageData[oldImages.length + 1];
			System.arraycopy(oldImages, 0, images, 0, oldImages.length);
			images[images.length - 1] = image;
			try {
				/* Read the 0-byte terminator at the end of the image. */
				id = inputStream.read();
				if (id > 0) {
					/* We read the terminator earlier. */
					inputStream.unread(new byte[] {(byte)id});
				}
			} catch (IOException e) {
				SWT.error(SWT.ERROR_IO, e);
			}
			getExtensions();
			id = readID();
		}
		return images;
	}

	/**
	 * Read and return the next block or extension identifier from the file.
	 */
	int readID() {
		try {
			return inputStream.read();
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
		}
		return -1;
	}

	/**
	 * Read extensions until an image descriptor appears.
	 * In the future, if we care about the extensions, they
	 * should be properly grouped with the image data before
	 * which they appeared. Right now, the interesting parts
	 * of some extensions are kept, but the rest is discarded.
	 * Throw an error if an error occurs.
	 */
	void getExtensions() {
		int id = readID();
		while (id != GIF_IMAGE_BLOCK_ID && id != GIF_TRAILER_ID && id > 0) {
			if (id == GIF_EXTENSION_BLOCK_ID) {
				readExtension();
			} else {
				SWT.error(SWT.ERROR_INVALID_IMAGE);
			}
			id = readID();
		}
		if (id == GIF_IMAGE_BLOCK_ID || id == GIF_TRAILER_ID) {
			try {
				inputStream.unread(new byte[] {(byte)id});
			} catch (IOException e) {
				SWT.error(SWT.ERROR_IO, e);
			}
		}
	}

	/**
	 * Read a control extension.
	 * Return the extension block data.
	 */
	byte[] readExtension() {
		int extensionID = readID();
		if (extensionID == GIF_COMMENT_BLOCK_ID)
			return readCommentExtension();
		if (extensionID == GIF_PLAIN_TEXT_BLOCK_ID)
			return readPlainTextExtension();
		if (extensionID == GIF_GRAPHICS_CONTROL_BLOCK_ID)
			return readGraphicsControlExtension();
		if (extensionID == GIF_APPLICATION_EXTENSION_BLOCK_ID)
			return readApplicationExtension();
		// Otherwise, we don't recognize the block. If the
		// field size is correct, we can just skip over
		// the block contents.
		try {
			int extSize = inputStream.read();
			if (extSize < 0) {
				SWT.error(SWT.ERROR_INVALID_IMAGE);
			}
			byte[] ext = new byte[extSize];
			inputStream.read(ext, 0, extSize);
			return ext;
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
			return null;
		}
	}

	/**
	 * We have just read the Comment extension identifier
	 * from the input stream. Read in the rest of the comment
	 * and return it. GIF comment blocks are variable size.
	 */
	byte[] readCommentExtension() {
		try {
			byte[] comment = new byte[0];
			byte[] block = new byte[255];
			int size = inputStream.read();
			while ((size > 0) && (inputStream.read(block, 0, size) != -1)) {
				byte[] oldComment = comment;
				comment = new byte[oldComment.length + size];
				System.arraycopy(oldComment, 0, comment, 0, oldComment.length);
				System.arraycopy(block, 0, comment, oldComment.length, size);
				size = inputStream.read();
			}
			return comment;
		} catch (Exception e) {
			SWT.error(SWT.ERROR_IO, e);
			return null;
		}
	}

	/**
	 * We have just read the PlainText extension identifier
	 * from the input stream. Read in the plain text info and text,
	 * and return the text. GIF plain text blocks are variable size.
	 */
	byte[] readPlainTextExtension() {
		try {
			// Read size of block = 0x0C.
			inputStream.read();
			// Read the text information (x, y, width, height, colors).
			byte[] info = new byte[12];
			inputStream.read(info);
			// Read the text.
			byte[] text = new byte[0];
			byte[] block = new byte[255];
			int size = inputStream.read();
			while ((size > 0) && (inputStream.read(block, 0, size) != -1)) {
				byte[] oldText = text;
				text = new byte[oldText.length + size];
				System.arraycopy(oldText, 0, text, 0, oldText.length);
				System.arraycopy(block, 0, text, oldText.length, size);
				size = inputStream.read();
			}
			return text;
		} catch (Exception e) {
			SWT.error(SWT.ERROR_IO, e);
			return null;
		}
	}

	/**
	 * We have just read the GraphicsControl extension identifier
	 * from the input stream. Read in the control information, store
	 * it, and return it.
	 */
	byte[] readGraphicsControlExtension() {
		try {
			// Read size of block = 0x04.
			inputStream.read();
			// Read the control block.
			byte[] controlBlock = new byte[4];
			inputStream.read(controlBlock);
			byte bitField = controlBlock[0];
			// Store the user input field.
			userInput = (bitField & 0x02) != 0;
			// Store the disposal method.
			disposalMethod = (bitField >> 2) & 0x07;
			// Store the delay time.
			delayTime = (controlBlock[1] & 0xFF) | ((controlBlock[2] & 0xFF) << 8);
			// Store the transparent color.
			if ((bitField & 0x01) != 0) {
				int colorIndex = controlBlock[3] & 0xFF;
				/* Work around: a customer has a GIF that specifies an
				 * invalid color index that is larger than the number
				 * of entries in the palette. Detect this case, and
				 * ignore the specified color index. */
				if (colorIndex <= 1 << defaultDepth) {
					transparentPixel = colorIndex;
				}
			} else {
				transparentPixel = -1;
			}
			// Read block terminator.
			inputStream.read();
			return controlBlock;
		} catch (Exception e) {
			SWT.error(SWT.ERROR_IO, e);
			return null;
		}
	}

	/**
	 * We have just read the Application extension identifier
	 * from the input stream.  Read in the rest of the extension,
	 * look for and store 'number of repeats', and return the data.
	 */
	byte[] readApplicationExtension() {
		try {
			// Read size of block = 0x0B.
			inputStream.read();
			// Read application identifier.
			byte[] applicationBytes = new byte[8];
			inputStream.read(applicationBytes);
			String application = new String(applicationBytes);
			// Read authentication code.
			byte[] authenticationBytes = new byte[3];
			inputStream.read(authenticationBytes);
			String authentication = new String(authenticationBytes);
			// Read application data.
			byte[] data = new byte[0];
			byte[] block = new byte[255];
			int size = inputStream.read();
			while ((size > 0) && (inputStream.read(block, 0, size) != -1)) {
				byte[] oldData = data;
				data = new byte[oldData.length + size];
				System.arraycopy(oldData, 0, data, 0, oldData.length);
				System.arraycopy(block, 0, data, oldData.length, size);
				size = inputStream.read();
			}
			// Look for the NETSCAPE 'repeat count' field for an animated GIF.
			if (application.equals("NETSCAPE") && authentication.equals("2.0") && data[0] == 01) { //$NON-NLS-1$ //$NON-NLS-2$
				repeatCount = (data[1] & 0xFF) | ((data[2] & 0xFF) << 8);
				loader.repeatCount = repeatCount;
			}
			return data;
		} catch (Exception e) {
			SWT.error(SWT.ERROR_IO, e);
			return null;
		}
	}

	/**
	 * Return a DeviceIndependentImage representing the
	 * image block at the current position in the input stream.
	 * Throw an error if an error occurs.
	 */
	ImageData readImageBlock(PaletteData defaultPalette) {
		int depth;
		PaletteData palette;
		byte[] block = new byte[9];
		try {
			inputStream.read(block);
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
		}
		int left = (block[0] & 0xFF) | ((block[1] & 0xFF) << 8);
		int top = (block[2] & 0xFF) | ((block[3] & 0xFF) << 8);
		int width = (block[4] & 0xFF) | ((block[5] & 0xFF) << 8);
		int height = (block[6] & 0xFF) | ((block[7] & 0xFF) << 8);
		byte bitField = block[8];
		boolean interlaced = (bitField & 0x40) != 0;
//		boolean sorted = (bitField & 0x20) != 0;
		if ((bitField & 0x80) != 0) {
			// Local palette.
			depth = (bitField & 0x7) + 1;
			palette = readPalette(1 << depth);
		} else {
			// No local palette.
			depth = defaultDepth;
			palette = defaultPalette;
		}
		// Promote depth to next highest supported value.
		if (!(depth == 1 || depth == 4 || depth == 8)) {
			if (depth < 4)
				depth = 4;
			else
				depth = 8;
		}
		if (palette == null) {
			palette = grayRamp(1 << depth);
		}
		int initialCodeSize = -1;
		try {
			initialCodeSize = inputStream.read();
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
		}
		if (initialCodeSize < 0) {
			SWT.error(SWT.ERROR_INVALID_IMAGE);
		}
		ImageData image = ImageData.internal_new(
			width,
			height,
			depth,
			palette,
			4,
			null,
			0,
			null,
			null,
			-1,
			transparentPixel,
			SWT.IMAGE_GIF,
			left,
			top,
			disposalMethod,
			delayTime);	
		LZWCodec codec = new LZWCodec();
		codec.decode(inputStream, loader, image, interlaced, initialCodeSize);
		return image;
	}

	/**
	 * Read a palette from the input stream.
	 */
	PaletteData readPalette(int numColors) {
		byte[] bytes = new byte[numColors * 3];
		try {
			if (inputStream.read(bytes) != bytes.length)
				SWT.error(SWT.ERROR_INVALID_IMAGE);
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
		}
		RGB[] colors = new RGB[numColors];
		for (int i = 0; i < numColors; i++)
			colors[i] = new RGB(bytes[i*3] & 0xFF, 
				bytes[i*3+1] & 0xFF, bytes[i*3+2] & 0xFF);
		return new PaletteData(colors);
	}

	/**
	 * Write the specified device independent image
	 * to the output stream.
	 */
	void unloadIntoByteStream(ImageData image) {
		if (!((image.depth == 1) || (image.depth == 4) || (image.depth == 8))) {
			SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
		}
		byte bitField = (byte)((0x80 & 0xF8 & 0xF7 & 0x8F) + (image.depth - 1) + ((image.depth - 1) * 16));
		try {
			outputStream.write(new byte[] { (byte)'G', (byte)'I', (byte)'F' });
			outputStream.write(new byte[] { (byte)'8', (byte)'9', (byte)'a' });
			outputStream.writeShort((short)image.width);
			outputStream.writeShort((short)image.height);
			outputStream.writeByte(bitField);
			outputStream.writeByte((byte)0);
			outputStream.writeByte((byte)0);
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
		}
		writePalette(image.palette, image.depth);
		if (image.transparentPixel != -1 || image.disposalMethod != 0 || image.delayTime != 0) {
			writeGraphicsControlBlock(image);
		}
		writeImageBlock(image);
	}

	/**
	 * Write out a GraphicsControlBlock to describe
	 * the specified device independent image.
	 */
	void writeGraphicsControlBlock(ImageData image) {
		try {
			outputStream.write(GIF_EXTENSION_BLOCK_ID);
			outputStream.write(GIF_GRAPHICS_CONTROL_BLOCK_ID);
			outputStream.write(0x04); // size of block
			byte[] gcBlock = new byte[4];
			gcBlock[0] = (byte)0xFD;
			gcBlock[1] = 0;
			gcBlock[2] = 0;
			gcBlock[3] = 0;
			if (image.transparentPixel == -1) {
				gcBlock[0] = (byte)(gcBlock[0] & 0xFE);
			} else {
				gcBlock[0] = (byte)(gcBlock[0] | 0x01);
				gcBlock[3] = (byte)image.transparentPixel;
			}
			if (image.disposalMethod != 0) {
				gcBlock[0] = (byte)(gcBlock[0] | ((image.disposalMethod & 0x07) << 2));
			}
			if (image.delayTime != 0) {
				gcBlock[1] = (byte)(image.delayTime & 0xFF);
				gcBlock[2] = (byte)((image.delayTime >> 8) & 0xFF);
			}
			outputStream.write(gcBlock);
			outputStream.write(0); // block terminator
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
		}
	}

	/**
	 * Write the specified device independent image
	 * to the current position in the output stream.
	 */
	void writeImageBlock(ImageData image) {
		try {
			outputStream.write(GIF_IMAGE_BLOCK_ID);
			byte[] block = new byte[9];
			block[0] = (byte)(image.x & 0xFF);
			block[1] = (byte)((image.x >> 8) & 0xFF);
			block[2] = (byte)(image.y & 0xFF);
			block[3] = (byte)((image.y >> 8) & 0xFF);
			block[4] = (byte)(image.width & 0xFF);
			block[5] = (byte)((image.width >> 8) & 0xFF);
			block[6] = (byte)(image.height & 0xFF);
			block[7] = (byte)((image.height >> 8) & 0xFF); 
			block[8] = 0; // no interlace, no sort, no local palette
			outputStream.write(block);
			outputStream.write(image.depth);
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
		}
		new LZWCodec().encode(outputStream, image);
	}

	/**
	 * Write the specified palette to the output stream.
	 */
	void writePalette(PaletteData palette, int depth) {
		byte[] bytes = new byte[(1 << depth) * 3];
		int offset = 0;
		for (int i = 0; i < palette.colors.length; i++) {
			RGB color = palette.colors[i];
			bytes[offset] = (byte)color.red;
			bytes[offset + 1] = (byte)color.green;
			bytes[offset + 2] = (byte)color.blue;
			offset += 3;;
		}
		try {
			outputStream.write(bytes);
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO, e);
		}
	}
}
