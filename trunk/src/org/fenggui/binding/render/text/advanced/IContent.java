package org.fenggui.binding.render.text.advanced;

import org.fenggui.util.Dimension;

public interface IContent<T>
{
	/**
	 * Adds a character at the selection start index. If a selection is present it will be
	 * removed.
	 * 
	 * @param c
	 * @return true if the character could be added.
	 */
	public abstract boolean addChar(char c);

	/**
	 * Adds content to this line.
	 * 
	 * @param content Content to add
	 * @param factory the factory to use
	 * @return true if content could be added, false otherwise.
	 */
	public abstract boolean addContent(String content, IContentFactory factory);

	/**
	 * @return Returns the atomCount.
	 */
	public abstract long getAtomCount();

	public abstract int getContentIndexOfAtom(int atom);
	
	public abstract long getIndexCount();
	
	public abstract String getContent();

	public abstract Dimension getSize();

	/**
	 * Merges the given ContentLine into this ContentLine. The new content
	 * will be added at the end of this line.
	 * @param line
	 * 
	 * will merge the given userline with this userline. The given userline will be added
	 * at the end of this userline.
	 */
	public abstract void mergeContent(T line);

	/**
	 * Optimizes the containing content parts by trying to merge parts together and
	 * removing empty parts. This should not change anything on the parts itself.
	 * 
	 * This method optimizes the parts of this ContentUserLine. After the optimization the
	 * ContentUserLine is one line height (so word-warping is removed) and contains the
	 * minimal amount of content parts possible.
	 */
	public abstract void optimizeContent();
	
	/**
	 * Removes all content from this Content object.
	 */
	public abstract void removeAll();
}