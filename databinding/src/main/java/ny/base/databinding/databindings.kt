package ny.base.databinding

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.MainThread
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

/**
 * reflection 이용
 */
@MainThread
inline fun <reified DB : ViewDataBinding> Activity.dataBindings(): Lazy<DB> {

    return ActivityDataBindingReflectionLazy(DB::class, this)
}

/**
 * ViewDataBinding 클래스의 inflate 반환 타입은 ViewDataBinding 상속 타입일 수 밖에 없다.
 */
@Suppress("UNCHECKED_CAST")
class ActivityDataBindingReflectionLazy< DB : ViewDataBinding>(
    private val dataBindingClass: KClass<DB>,
    private val activity: Activity,
) : Lazy<DB> {
    private var cached: DB? = null

    override val value: DB
        get() {
            return cached
                ?: return (dataBindingClass.java.getMethod("inflate", LayoutInflater::class.java)(
                    activity,
                    activity.layoutInflater
                ) as DB).also {
                    cached = it
                }
        }

    override fun isInitialized(): Boolean = cached != null
}

/**
 * reflection 이용
 */
@MainThread
inline fun <reified DB : ViewDataBinding> Fragment.dataBindings(): Lazy<DB> {

    return FragmentDataBindingReflectionLazy(DB::class, this)
}

/**
 * ViewDataBinding 클래스의 inflate 반환 타입은 ViewDataBinding 상속 타입일 수 밖에 없다.
 */
@Suppress("UNCHECKED_CAST")
class FragmentDataBindingReflectionLazy< DB : ViewDataBinding>(
    private val dataBindingClass: KClass<DB>,
    private val fragment: Fragment,
) : Lazy<DB> {
    private var cached: DB? = null

    override val value: DB
        get() {
            return cached
                ?: return (dataBindingClass.java.getMethod("inflate", LayoutInflater::class.java)(
                    fragment,
                    fragment.layoutInflater
                ) as DB).also {
                    cached = it
                }
        }

    override fun isInitialized(): Boolean = cached != null
}

/**
 *  DataBindingUtil 이용
 */
@MainThread
inline fun <reified DB : ViewDataBinding> Activity.dataBindings(@LayoutRes layoutRes: Int): Lazy<DB> {
    return ActivityDataBindingLazy(this, layoutRes)
}

class ActivityDataBindingLazy<out DB : ViewDataBinding>(
    private val activity: Activity,
    @LayoutRes private val layoutRes: Int
) : Lazy<DB> {
    private var cached: DB? = null

    override val value: DB
        get() {
            val dataBinding = cached
            return if (dataBinding == null) {
                cached = DataBindingUtil.setContentView(activity, layoutRes)
                cached!!
            } else {
                dataBinding
            }
        }

    override fun isInitialized(): Boolean = cached != null
}

/**
 *  DataBindingUtil 이용
 */
@MainThread
inline fun <reified DB : ViewDataBinding> Fragment.dataBindings(@LayoutRes layoutRes: Int): Lazy<DB> {
    return FragmentDataBindingLazy(this, layoutRes)
}

class FragmentDataBindingLazy<out DB : ViewDataBinding>(
    private val fragment: Fragment,
    @LayoutRes private val layoutRes: Int
) : Lazy<DB> {
    private var cached: DB? = null

    override val value: DB
        get() {
            val dataBinding = cached
            return if (dataBinding == null) {
                cached = DataBindingUtil.inflate(
                    fragment.layoutInflater,
                    layoutRes,
                    fragment.requireView() as ViewGroup,
                    false
                )
                cached!!
            } else {
                dataBinding
            }
        }

    override fun isInitialized(): Boolean = cached != null
}