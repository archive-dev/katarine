package io.github.whoisamyy.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.components.DrawableComponent;
import io.github.whoisamyy.katarine.annotations.EditorObject;
import io.github.whoisamyy.utils.Utils;
import io.github.whoisamyy.utils.math.shapes.Rect;
import io.github.whoisamyy.utils.render.RectOwner;
import io.github.whoisamyy.utils.serialization.annotations.Range;

import static com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.DEFAULT_CHARS;

@EditorObject
public class Text extends DrawableComponent implements RectOwner {
    public String text = "example text";
    public boolean show = true;

    //copied from libgdx tutorial

    /** The size in units (for some reason) */
    private int size = 16;
    /** Foreground color (required for non-black borders) */
    private Color color = Color.WHITE;
    /** Border width in pixels, 0 to disable */
    private float borderWidth = 0;
    /** Border color; only used if borderWidth > 0 */
    private Color borderColor = Color.BLACK;
    /** true for straight (mitered), false for rounded borders */
    private boolean borderStraight = false;
    /** Offset of text shadow on X axis in pixels, 0 to disable */
    private int shadowOffsetX = 0;
    /** Offset of text shadow on Y axis in pixels, 0 to disable */
    private int shadowOffsetY = 0;
    /** Shadow color; only used if shadowOffset > 0 */
    private Color shadowColor = new Color(0, 0, 0, 0.75f);
    /** The characters the font should contain */
    private String characters = DEFAULT_CHARS;
    /** Whether the font should include kerning */
    private boolean kerning = true;
    /** The optional PixmapPacker to use */
    private PixmapPacker packer = null;
    /** Whether to flip the font vertically */
    private boolean flip = false;
    /** Whether to generate mip maps for the resulting texture */
    private boolean genMipMaps = true;
    /** Minification filter */
    private Texture.TextureFilter minFilter = Texture.TextureFilter.Linear;
    /** Magnification filter */
    private Texture.TextureFilter magFilter = Texture.TextureFilter.Linear;
    private BitmapFont font;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private String fontFile;
    @Range.FloatRange(min = 0.00000001f)
    private float sizeXY;

    GlyphLayout glyphLayout = null;

    private Vector2 pos = new Vector2();

    public Text(String fontFile, float sizeXY, int size, Color color, float borderWidth, Color borderColor, boolean borderStraight, int shadowOffsetX, int shadowOffsetY, Color shadowColor, String characters, boolean kerning, PixmapPacker packer, boolean flip, boolean genMipMaps, Texture.TextureFilter minFilter, Texture.TextureFilter magFilter, boolean ui) {
        super(ui);
        this.fontFile = fontFile;
        this.size = size;
        this.color = color;
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
        this.borderStraight = borderStraight;
        this.shadowOffsetX = shadowOffsetX;
        this.shadowOffsetY = shadowOffsetY;
        this.shadowColor = shadowColor;
        this.characters = characters;
        this.kerning = kerning;
        this.packer = packer;
        this.flip = flip;
        this.genMipMaps = genMipMaps;
        this.minFilter = minFilter;
        this.magFilter = magFilter;

        this.parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        this.parameter.size = size;
        this.parameter.color = color;
        this.parameter.borderWidth = borderWidth;
        this.parameter.borderColor = borderColor;
        this.parameter.borderStraight = borderStraight;
        this.parameter.shadowOffsetX = shadowOffsetX;
        this.parameter.shadowOffsetY = shadowOffsetY;
        this.parameter.shadowColor = shadowColor;
        this.parameter.characters = characters;
        this.parameter.kerning = kerning;
        this.parameter.packer = packer;
        this.parameter.flip = flip;
        this.parameter.genMipMaps = genMipMaps;
        this.parameter.minFilter = minFilter;
        this.parameter.magFilter = magFilter;
        this.sizeXY = sizeXY;
    }

    /**
     * Do not recommend to use because of size issues. Better use {@link Text#Text(String, float, Color, float, Color, boolean, boolean)}
     * @param size breaks on size > ~128
     */
    public Text(String fontFile, float sizeXY, int size, Color color, float borderWidth, Color borderColor, boolean borderStraight, boolean ui) {
        this(fontFile, sizeXY, size, color, borderWidth, borderColor, borderStraight, 0, 0, Color.BLACK, DEFAULT_CHARS, true, null, false, false, Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest, ui);
    }

    public Text(String fontFile, float size, Color color, float borderWidth, Color borderColor, boolean borderStraight, boolean ui) {
        this(fontFile, size, 128, color, borderWidth, borderColor, borderStraight, 0, 0, Color.BLACK, DEFAULT_CHARS, true, null, false, false, Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest, ui);
    }

    @Override
    public void awake() {
        this.font = new FreeTypeFontGenerator(Gdx.files.internal(fontFile)).generateFont(this.parameter);
        this.font.getData().setScale(sizeXY / Utils.PPU);
        this.font.setUseIntegerPositions(false);
    }

    @Override
    public void start() {
        this.glyphLayout = new GlyphLayout(font, text);
        pos.add(transform.pos);
    }

    @Override
    protected void draw() {
        this.font.getData().setScale(sizeXY / Utils.PPU);
        glyphLayout = new GlyphLayout(font, text);
        Vector2 v2 = transform.pos.cpy().add(pos).sub(glyphLayout.width / 2, -glyphLayout.height / 2);
        font.draw(this.batch, text, v2.x, v2.y);
    }

    @Override
    public void die() {
        if (font!=null)
            font.dispose();
    }

    public String getText() {
        return text;
    }

    public boolean isShow() {
        return show;
    }

    public int getSize() {
        return size;
    }

    public Color getColor() {
        return color;
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public boolean isBorderStraight() {
        return borderStraight;
    }

    public int getShadowOffsetX() {
        return shadowOffsetX;
    }

    public int getShadowOffsetY() {
        return shadowOffsetY;
    }

    public Color getShadowColor() {
        return shadowColor;
    }

    public String getCharacters() {
        return characters;
    }

    public boolean isKerning() {
        return kerning;
    }

    public PixmapPacker getPacker() {
        return packer;
    }

    public boolean isFlip() {
        return flip;
    }

    public boolean isGenMipMaps() {
        return genMipMaps;
    }

    public Texture.TextureFilter getMinFilter() {
        return minFilter;
    }

    public Texture.TextureFilter getMagFilter() {
        return magFilter;
    }

    public BitmapFont getFont() {
        return font;
    }

    public FreeTypeFontGenerator.FreeTypeFontParameter getParameter() {
        return parameter;
    }

    public String getFontFile() {
        return fontFile;
    }

    public float getSizeXY() {
        return sizeXY;
    }

    public Vector2 getPos() {
        return pos;
    }

    public void setSize(int size) {
        this.parameter.size = size;
        this.size = size;
        awake();
    }

    public void setColor(Color color) {
        this.parameter.color = color;
        this.color = color;
        awake();
    }

    public void setBorderWidth(float borderWidth) {
        this.parameter.borderWidth = borderWidth;
        this.borderWidth = borderWidth;
        awake();
    }

    public void setBorderColor(Color borderColor) {
        this.parameter.borderColor = borderColor;
        this.borderColor = borderColor;
        awake();
    }

    public void setBorderStraight(boolean borderStraight) {
        this.parameter.borderStraight = borderStraight;
        this.borderStraight = borderStraight;
        awake();
    }

    public void setShadowOffsetX(int shadowOffsetX) {
        this.parameter.shadowOffsetX = shadowOffsetX;
        this.shadowOffsetX = shadowOffsetX;
        awake();
    }

    public void setShadowOffsetY(int shadowOffsetY) {
        this.parameter.shadowOffsetY = shadowOffsetY;
        this.shadowOffsetY = shadowOffsetY;
        awake();
    }

    public void setShadowColor(Color shadowColor) {
        this.parameter.shadowColor = shadowColor;
        this.shadowColor = shadowColor;
        awake();
    }

    public void setCharacters(String characters) {
        this.parameter.characters = characters;
        this.characters = characters;
        awake();
    }

    public void setKerning(boolean kerning) {
        this.parameter.kerning = kerning;
        this.kerning = kerning;
        awake();
    }

    public void setPacker(PixmapPacker packer) {
        this.parameter.packer = packer;
        this.packer = packer;
        awake();
    }

    public void setFlip(boolean flip) {
        this.parameter.flip = flip;
        this.flip = flip;
        awake();
    }

    public void setGenMipMaps(boolean genMipMaps) {
        this.parameter.genMipMaps = genMipMaps;
        this.genMipMaps = genMipMaps;
        awake();
    }

    public void setMinFilter(Texture.TextureFilter minFilter) {
        this.parameter.minFilter = minFilter;
        this.minFilter = minFilter;
        awake();
    }

    public void setMagFilter(Texture.TextureFilter magFilter) {
        this.parameter.magFilter = magFilter;
        this.magFilter = magFilter;
        awake();
    }

    public void setSizeXY(float sizeXY) {
        this.sizeXY = sizeXY;
        //font.getData().setScale(sizeXY/Utils.PPU);
    }

    public void setFontFile(String fontFile) {
        this.fontFile = fontFile;
        awake();
    }

    public float getTextWidth() {
        glyphLayout = new GlyphLayout(font, text);
        return glyphLayout!=null?glyphLayout.width:0;
    }

    public float getTextHeight() {
        glyphLayout = new GlyphLayout(font, text);
        return glyphLayout!=null?glyphLayout.height:0;
    }

    @Override
    public Rect getRect() {
        Rect r = new Rect(getTextWidth(), getTextHeight());
        return r;
    }
}