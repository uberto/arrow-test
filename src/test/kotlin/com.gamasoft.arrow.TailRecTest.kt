import assertk.assert
import assertk.assertAll
import assertk.assertions.*
import com.gamasoft.arrow.population_count
import org.junit.Test



class TailRecTest {

    @Test
    fun zeroHasNoOnes() {
        assert(population_count(0)).isEqualTo(0)
    }

    @Test
    fun powerOfTwoHasOneOnes() {
        assertAll {
            assert(population_count(1)).isEqualTo(1)
            assert(population_count(2)).isEqualTo(1)
            assert(population_count(4)).isEqualTo(1)
            assert(population_count(8)).isEqualTo(1)
            assert(population_count(16)).isEqualTo(1)
            assert(population_count(32)).isEqualTo(1)
            assert(population_count(64)).isEqualTo(1)
            assert(population_count(128)).isEqualTo(1)
            assert(population_count(256)).isEqualTo(1)
            assert(population_count(512)).isEqualTo(1)
        }
    }

    @Test
    fun upToTen() {
        assertAll {
            assert(population_count(3)).isEqualTo(2)
            assert(population_count(5)).isEqualTo(2)
            assert(population_count(6)).isEqualTo(2)
            assert(population_count(7)).isEqualTo(3)
            assert(population_count(9)).isEqualTo(2)
        }
    }

    @Test
    fun twoOnes() {
        assertAll {
            assert(population_count(15)).isEqualTo(4)
            assert(population_count(19)).isEqualTo(3)
            assert(population_count(255)).isEqualTo(8)
            assert(population_count(1023)).isEqualTo(10)
        }
    }

    @Test
    fun bigNumber() {
        assertAll {
            assert(population_count(Int.MAX_VALUE)).isEqualTo(31)
        }
    }

}
