package sn.tools.swing.xml.component;

import java.awt.Dimension;
import java.lang.reflect.Field;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import sn.tools.xml.bind.annotation.InjectXmlAttribute;

public interface XmlComponent {

    @InjectXmlAttribute("width")
    default void setWidth(String width) {
        if (this instanceof JComponent comp) {
            Dimension preferredSize = comp.getPreferredSize();
            comp.setPreferredSize(new Dimension(Integer.parseInt(width), preferredSize.height));
        }
    }

    @InjectXmlAttribute("height")
    default void setHeight(String height) {
        if (this instanceof JComponent comp) {
            Dimension preferredSize = comp.getPreferredSize();
            comp.setPreferredSize(new Dimension(preferredSize.width, Integer.parseInt(height)));
        }
    }

    @InjectXmlAttribute("value")
    default void setValue(String value) {
        if (this instanceof JTextComponent comp) {
            comp.setText(value);
        } else if (this instanceof AbstractButton comp) {
            comp.setText(value);
        } else if (this instanceof JLabel comp) {
            comp.setText(value);
        }
    }

    @InjectXmlAttribute("label")
    default void setLabel(String label) {
        setValue(label);
    }

    @InjectXmlAttribute("selected")
    default void setSelected(String selected) {
        if (this instanceof AbstractButton btn) {
            btn.setSelected(Boolean.parseBoolean(selected));
        }
    }

    @InjectXmlAttribute("checked")
    default void setChecked(String checked) {
        setSelected(checked);
    }

    @InjectXmlAttribute("tooltip")
    default void setTooltip(String tooltip) {
        if (this instanceof JComponent comp) {
            comp.setToolTipText(tooltip);
        }
    }

    @InjectXmlAttribute("h-align")
    default void setHorizontalAlignment(String alignment) {
        if (this instanceof JTextField comp) {
            try {
                Field field = JTextField.class.getField(alignment.toUpperCase());
                comp.setHorizontalAlignment(field.getInt(null));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new IllegalArgumentException("Invalid alignment: " + alignment, e);
            }
        } else if (this instanceof JLabel comp) {
            try {
                Field field = JLabel.class.getField(alignment.toUpperCase());
                comp.setHorizontalAlignment(field.getInt(null));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new IllegalArgumentException("Invalid alignment: " + alignment, e);
            }
        } else if (this instanceof AbstractButton comp) {
            try {
                Field field = AbstractButton.class.getField(alignment.toUpperCase());
                comp.setHorizontalAlignment(field.getInt(null));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new IllegalArgumentException("Invalid alignment: " + alignment, e);
            }
        }
    }

    @InjectXmlAttribute("v-align")
    default void setVerticalAlignment(String alignment) {
        if (this instanceof JLabel comp) {
            try {
                Field field = JLabel.class.getField(alignment.toUpperCase());
                comp.setVerticalAlignment(field.getInt(null));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new IllegalArgumentException("Invalid vertical alignment: " + alignment, e);
            }
        } else if (this instanceof AbstractButton comp) {
            try {
                Field field = AbstractButton.class.getField(alignment.toUpperCase());
                comp.setVerticalAlignment(field.getInt(null));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new IllegalArgumentException("Invalid vertical alignment: " + alignment, e);
            }
        }
    }

}
