package org.geogebra.web.full.gui.toolbar.mow;

import org.geogebra.common.euclidian.EuclidianConstants;
import org.geogebra.common.gui.AccessibilityGroup;
import org.geogebra.common.gui.toolbar.ToolBar;
import org.geogebra.common.main.Feature;
import org.geogebra.web.full.main.embed.H5PLoader;
import org.geogebra.web.html5.main.AppW;

/**
 * Submenu for media (i.e. photo, video, ...)
 * 
 * @author Alicia Hofstaetter
 *
 */
public class MediaSubMenu extends SubMenuPanel {
	/**
	 * @param app
	 *            application
	 */
	public MediaSubMenu(AppW app) {
		super(app);
		addStyleName("mediaSubMenu");
	}

	@Override
	protected void createContentPanel() {
		super.createContentPanel();
		boolean graspableMath = app.getVendorSettings().isGraspableMathEnabled();
		boolean h5p = app.getVendorSettings().isH5PEnabled();
		if (h5p) {
			// TODO: move this to the chooser dialog if it will be implemented.
			H5PLoader.INSTANCE.load();
//			JavaScriptInjector.inject(GuiResourcesSimple.INSTANCE.h5pViewerJs());
		}
		super.createPanelRow(ToolBar.getMOWMediaToolBarDefString(graspableMath, h5p));
		makeButtonsAccessible(AccessibilityGroup.NOTES_TOOL_MEDIA);
	}

	@Override
	public int getFirstMode() {
		return getTextMode(app);
	}

	/**
	 * Chooses text mode - for development.
	 * 
	 * @param app
	 *            see {@link AppW}
	 * 
	 * @return the text mode for the tool.
	 */
	public static int getTextMode(AppW app) {
		return app.has(Feature.MOW_TEXT_TOOL)
				? EuclidianConstants.MODE_MEDIA_TEXT
				: EuclidianConstants.MODE_TEXT;
	}

	@Override
	public boolean isValidMode(int mode) {
		return mode == getTextMode(app) || mode == EuclidianConstants.MODE_IMAGE
				|| mode == EuclidianConstants.MODE_CAMERA
				|| mode == EuclidianConstants.MODE_VIDEO
				|| mode == EuclidianConstants.MODE_AUDIO
				|| mode == EuclidianConstants.MODE_GRAPHING
				|| mode == EuclidianConstants.MODE_PDF
				|| mode == EuclidianConstants.MODE_EXTENSION
				|| mode == EuclidianConstants.MODE_TABLE
				|| mode == EuclidianConstants.MODE_EQUATION
				|| mode == EuclidianConstants.MODE_GRASPABLE_MATH
				|| mode == EuclidianConstants.MODE_CAS
				|| (app.isMebis() && mode == EuclidianConstants.MODE_H5P);
	}
}
