/**
 * This file is part of the ReTeX library - https://github.com/himamis/ReTeX
 *
 * Copyright (C) 2015 Balazs Bencze
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 *
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 *
 * Linking this library statically or dynamically with other modules 
 * is making a combined work based on this library. Thus, the terms 
 * and conditions of the GNU General Public License cover the whole 
 * combination.
 * 
 * As a special exception, the copyright holders of this library give you 
 * permission to link this library with independent modules to produce 
 * an executable, regardless of the license terms of these independent 
 * modules, and to copy and distribute the resulting executable under terms 
 * of your choice, provided that you also meet, for each linked independent 
 * module, the terms and conditions of the license of that module. 
 * An independent module is a module which is not derived from or based 
 * on this library. If you modify this library, you may extend this exception 
 * to your version of the library, but you are not obliged to do so. 
 * If you do not wish to do so, delete this exception statement from your 
 * version.
 * 
 */
package com.himamis.retex.renderer.web.font.opentype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.ScriptInjector;
import com.himamis.retex.renderer.web.font.FontLoaderWrapper;
import com.himamis.retex.renderer.web.font.FontW;
import com.himamis.retex.renderer.web.resources.js.JsResources;

public class Opentype implements FontLoaderWrapper {

	public static final Opentype INSTANCE = new Opentype();

	private static class FontContainer {
		public OpentypeFontWrapper font;
		public boolean fontIsLoading;

		public FontContainer(OpentypeFontWrapper font) {
			this.font = font;
			this.fontIsLoading = true;
		}
	}

	private List<OpentypeFontStatusListener> listeners;
	private Map<String, FontContainer> fonts;
	private String fontBaseUrl = GWT.getModuleBaseURL();

	private Opentype() {
		ScriptInjector.fromString(JsResources.INSTANCE.opentypeJs().getText())
				.setRemoveTag(false).inject();
		listeners = new ArrayList<OpentypeFontStatusListener>();
		fonts = new HashMap<String, FontContainer>();
	}

	public void addListener(OpentypeFontStatusListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	public void removeListener(OpentypeFontStatusListener listener) {
		listeners.remove(listener);
	}

	private void fireFontActiveEvent(String familyName) {
		OpentypeFontWrapper fontWrapper = fonts.get(familyName).font;

		// a copy of the listeners is needed, because listeners are being
		// removed
		// from the list throughout the iteration.
		// see OpentypeFont::onFontLoaded(..)
		List<OpentypeFontStatusListener> copyList = new ArrayList<OpentypeFontStatusListener>(
				listeners);

		for (OpentypeFontStatusListener listener : copyList) {
			listener.onFontLoaded(fontWrapper, familyName);
		}
	}

	private void fireFontInactiveEvent(Object error, String familyName) {
		for (OpentypeFontStatusListener listener : listeners) {
			listener.onFontError(error, familyName);
		}
	}

	private boolean fontEntryExists(String familyName) {
		return fonts.get(familyName) != null;
	}

	private void createFontEntry(String familyName) {
		FontContainer fontContainer = new FontContainer(null);
		fonts.put(familyName, fontContainer);
	}

	boolean fontIsLoading(String familyName) {
		FontContainer fontContainer = fonts.get(familyName);
		return fontContainer != null && fontContainer.fontIsLoading;
	}

	boolean fontIsLoaded(String familyName) {
		FontContainer fontContainer = fonts.get(familyName);
		return fontContainer != null && !fontContainer.fontIsLoading;
	}

	OpentypeFontWrapper getFont(String familyName) {
		return fonts.get(familyName).font;
	}

	private void setFontIsLoaded(String familyName, JavaScriptObject font) {
		FontContainer fontContainer = fonts.get(familyName);
		fontContainer.font = new OpentypeFontWrapper(font);
		fontContainer.fontIsLoading = false;
	}

	private void loadFont(String path, String familyName) {
		// font does not exist
		if (!fontEntryExists(familyName)) {
			createFontEntry(familyName);
			loadJavascriptFont(fontBaseUrl + path, familyName);
		} else if (fontIsLoading(familyName)) {
			// do nothing, wait for the font to be loaded
		} else if (fontIsLoaded(familyName)) {
			fireFontActiveEvent(familyName);
		}
	}

	private void loadJavascriptFont(String path, final String familyName) {
		path = path.substring(0, path.length() - 3);
		path = path + "js";

		ScriptInjector.fromUrl(path).setWindow(ScriptInjector.TOP_WINDOW)
				.setRemoveTag(true)
				.setCallback(new Callback<Void, Exception>() {

					public void onFailure(Exception reason) {
						fireFontInactiveEvent(reason, familyName);
					}

					public void onSuccess(Void result) {
						nativeParseFont(familyName);
					}
				}).inject();
	}

	private native void nativeParseFont(String familyName) /*-{
		var that = this;
		var base64EncodedData = $wnd.__JLM_GWT_FONTS__[familyName];
		var decodedArrayBuffer = that.@com.himamis.retex.renderer.web.font.opentype.Opentype::base64ToArrayBuffer(Ljava/lang/String;)(base64EncodedData)
		var font = opentype.parse(decodedArrayBuffer);
		if (!font.supported) {
			that.@com.himamis.retex.renderer.web.font.opentype.Opentype::fireFontInactiveEvent(Ljava/lang/Object;Ljava/lang/String;)("Parse error: font not supported", familyName);
		} else {
			that.@com.himamis.retex.renderer.web.font.opentype.Opentype::setFontIsLoaded(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(familyName, font);
			that.@com.himamis.retex.renderer.web.font.opentype.Opentype::fireFontActiveEvent(Ljava/lang/String;)(font.familyName);

		}
	}-*/;

	private native JavaScriptObject base64ToArrayBuffer(String base64) /*-{
		var binaryString = window.atob(base64);
		var length = binaryString.length;
		var bytes = new Uint8Array(length);
		for (var i = 0; i < length; i++) {
			bytes[i] = binaryString.charCodeAt(i);
		}
		return bytes.buffer;
	}-*/;

	@Override
	public FontW createNativeFont(String pathName, String fontName, int style,
			int size) {
		loadFont(pathName, fontName);
		return new OpentypeFont(fontName, style, size);
	}

	/**
	 * Sets the base URL from where the fonts are loaded.
	 * 
	 * @param url
	 *            base URL
	 */
	public void setFontBaseUrl(String url) {
		fontBaseUrl = url;
	}
}
