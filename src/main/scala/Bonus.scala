object Bonus {

  type Set = Int => Boolean

  def forall(s: Set, p: Int => Boolean): Boolean = {
    val bound = 1000
    def contains(s: Set, elem: Int): Boolean = s(elem)

    def iter(a: Int): Boolean = {
      if (a > bound) true
      else if (contains(s, a) && !p(a)) false
      else iter(a + 1)
    }
    iter(-bound)
  }

}
