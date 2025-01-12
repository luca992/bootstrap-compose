package app.softwork.bootstrapcompose

import androidx.compose.runtime.*
import kotlinx.uuid.*
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.*

/**
 * Bootstrap Navbar component. This version provides more flexibility than the alternative but is more complex
 * to use. For a simpler implementation use the version that takes a brand and navItems as parameters.
 *
 * @param collapseBehavior Specifies the Navbar's Responsive behavior with use of the .navbar-expand class.
 * @param fluid Specifies if the inner container is fluid (container-fluid) or not.
 * @param placement Specifies the placement of the navbar
 * @param containerBreakpoint Breakpoint for the inner container.
 * @param colorScheme Valid values are Color.Light or Color.Dark to set the navbar-dark/light class.
 * @param backgroundColor Background color to use, with the bg-* classes.
 * @param content Navbar content, placed within the inner container.
 */
@Composable
public fun Navbar(
    collapseBehavior: NavbarCollapseBehavior = NavbarCollapseBehavior.Always,
    fluid: Boolean = false,
    placement: NavbarPlacement = NavbarPlacement.Default,
    containerBreakpoint: Breakpoint? = null,
    colorScheme: Color = Color.Light,
    backgroundColor: Color = Color.Primary,
    styling: (Styling.() -> Unit)? = null,
    attrs: AttrBuilderContext<HTMLElement>? = null,
    content: ContentBuilder<HTMLDivElement>? = null
) {
    val classes = styling?.let {
        Styling().apply(it).generate()
    } ?: arrayOf()

    Nav(attrs = {
        classes(
            BSClasses.navbar,
            "navbar-${colorScheme}",
            "bg-${backgroundColor}"
        )
        when (placement) {
            NavbarPlacement.Default -> {
            }
            else -> {
                classes(placement.toString())
            }
        }

        classes(* classes)
        when (collapseBehavior) {
            is NavbarCollapseBehavior.Never -> classes("navbar-expand")
            is NavbarCollapseBehavior.AtBreakpoint -> classes("navbar-expand-${collapseBehavior.breakpoint}")
            is NavbarCollapseBehavior.Always -> {
            } //No class needed for "Always" behavior
        }
        attr("role", "navigation")
        attrs?.invoke(this)
    }) {
        Container(fluid, containerBreakpoint, content = content)
    }
}

/**
 * Bootstrap Navbar component that can optionally enable a Toggler and Brand.
 *
 * @param collapseBehavior Specifies the Navbar's Responsive behavior with use of the .navbar-expand class.
 * @param fluid Specifies if the inner container is fluid (container-fluid) or not.
 * @param stickyTop if true, shows the navbar at top
 * @param containerBreakpoint Breakpoint for the inner container.
 * @param colorScheme Valid values are Color.Light or Color.Dark to set the navbar-dark/light class.
 * @param backgroundColor Background color to use, with the bg-* classes.
 * @param toggler Whether or not a toggler button should be provided when falling below the breakpoint.
 * @param togglerPosition Specifies if the toggler should be placed on the left or right side of the Navbar.
 * @param togglerAttrs Additional attributes to set on the toggler button
 * @param togglerTargetId Optional id of the toggler, using a random UUID by default
 * @param attrs Additional attributes to set on the Navbar
 * @param navAttrs Additional attributes to set on the navItems section of the Navbar
 * @param brand Composable implementing the brand elements
 * @param navItems navigation items, normally comprised of NavbarLink and NavbarDropDown menu items.
 */
@Composable
public fun Navbar(
    collapseBehavior: NavbarCollapseBehavior = NavbarCollapseBehavior.Always,
    fluid: Boolean = false,
    placement: NavbarPlacement = NavbarPlacement.Default,
    containerBreakpoint: Breakpoint? = null,
    colorScheme: Color = Color.Light,
    backgroundColor: Color = Color.Primary,
    toggler: Boolean = true,
    togglerPosition: TogglerPosition = TogglerPosition.Right,
    togglerTargetId: String = remember { "toggler${UUID()}" },
    togglerAttrs: AttrBuilderContext<HTMLButtonElement>? = null,
    styling: (Styling.() -> Unit)? = null,
    attrs: AttrBuilderContext<HTMLElement>? = null,
    navAttrs: AttrBuilderContext<HTMLDivElement>? = null,
    brand: ContentBuilder<HTMLDivElement>,
    navItems: ContentBuilder<HTMLDivElement>
) {
    Navbar(
        collapseBehavior,
        fluid,
        placement,
        containerBreakpoint,
        colorScheme,
        backgroundColor,
        styling,
        attrs
    ) {
        if (togglerPosition == TogglerPosition.Right) {
            brand()
        }

        if (toggler) {
            Toggler(
                target = togglerTargetId,
                controls = togglerTargetId,
                styling = styling,
                attrs = togglerAttrs
            )
            NavbarCollapse(togglerTargetId) {
                NavbarNav(attrs = { navAttrs?.invoke(this) }) {
                    navItems()
                }
            }
        } else {
            NavbarNav {
                navItems()
            }
        }

        if (togglerPosition == TogglerPosition.Left) {
            brand()
        }
    }
}


/**
 * Bootstrap navbar-collapse component to be used with the NavbarToggler.
 * @param id The element id. This value should also be used as the target parameter with a NavbarToggler.
 */
@Composable
public fun NavbarCollapse(
    id: String,
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    content: ContentBuilder<HTMLDivElement>? = null
) {
    Div(attrs = {
        classes(BSClasses.collapse, BSClasses.navbarCollapse)
        id(id)
        attrs?.invoke(this)
    }, content = content)
}

/**
 * Bootstrap navbar-nav component to be used within a Navbar.
 */
@Composable
public fun NavbarNav(
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    links: ContentBuilder<HTMLDivElement>? = null
) {
    Div(attrs = {
        classes(BSClasses.navbarNav)
        attrs?.invoke(this)
    }, content = links)
}


/**
 * Bootstrap nav-link component.
 */
@Composable
public fun NavbarLink(
    active: Boolean,
    attrs: AttrBuilderContext<HTMLAnchorElement>? = null,
    disabled: Boolean = false,
    link: String? = null,
    content: ContentBuilder<HTMLAnchorElement>? = null
) {
    A(attrs = {
        classes(BSClasses.navLink)
        if (active) {
            classes(BSClasses.active)
            attr("aria-current", "page")
        }
        if (disabled) {
            classes(BSClasses.disabled)
        }
        attrs?.invoke(this)
    }, href = link, content = content)
}

public enum class TogglerPosition {
    Left, Right
}

public sealed class NavbarCollapseBehavior {
    public object Never : NavbarCollapseBehavior()
    public object Always : NavbarCollapseBehavior()
    public data class AtBreakpoint(val breakpoint: Breakpoint) : NavbarCollapseBehavior()
}

public enum class NavbarPlacement(private val prefix: String) {
    Default(""), FixedTop("fixed-top"), FixedBottom("fixed-bottom"), StickyTop("sticky-top");

    override fun toString(): String = prefix
}
