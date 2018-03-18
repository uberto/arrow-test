package com.gamasoft.arrow.monads

import kotlin.coroutines.experimental.buildSequence


enum class Candy { Red, Green, Blue, Yellow, Black }

enum class Input {Coin, Turn}

sealed class DispenserState{

    data class Locked(val candiesLeft: Int): DispenserState()
    data class Unlocked(val candiesLeft: Int): DispenserState()

    object Empty: DispenserState()
}

data class CandyDispenser(val state: DispenserState, val seed: Seed, val candy: Candy?)



typealias Seed = Int

fun nextInt(seed: Seed): Pair<Int, Seed> {
    val b = (seed + 1234) % 10000
    val newSeed = (b * b % 1000000) / 100
    val newVal = newSeed % 1000
    return newVal to newSeed
}

fun randoms(seed: Seed): Sequence<Int>{
    return generateSequence( 0 to seed) { nextInt(it.second) }.map{it.first}
}


fun toCandy(num: Int): Candy {
    val cc = Candy.values()
    val x = num % cc.size
    return cc.get(x)
}


fun nextCandy(seed: Seed): Pair<Candy, Seed>{
    val (x, ns) = nextInt(seed)
    return toCandy(x) to ns

}

fun candies(seed: Seed): Sequence<Candy> {
    //  another style, compare with randoms
    return buildSequence {
        var currSeed = seed

        // this sequence is infinite
        while (true) {
            val terms = nextCandy(currSeed)
            currSeed = terms.second
            yield(terms.first)
        }
    }
}

//------

fun processInput(rcd: CandyDispenser, input: Input): CandyDispenser {
    val cd = rcd.state
    return when (cd) {
        is DispenserState.Locked -> if (input == Input.Coin) CandyDispenser(DispenserState.Unlocked(cd.candiesLeft), rcd.seed, null) else rcd
        is DispenserState.Unlocked -> if (input == Input.Turn) turnHandle(cd, rcd.seed) else rcd
        else -> rcd
    }

}

fun turnHandle(cd: DispenserState.Unlocked, seed:Int): CandyDispenser {
    val (newCandy, newSeed) = nextCandy(seed)
    val candiesLeft = cd.candiesLeft - 1

    return if (cd.candiesLeft == 0)
        CandyDispenser(DispenserState.Empty, newSeed, newCandy)
    else
        CandyDispenser(DispenserState.Locked(candiesLeft), newSeed, newCandy)
}

fun processInputs(rcd: CandyDispenser, inputs: List<Input>): Pair<CandyDispenser, List<Candy>>{

    fun op(acc: Pair<CandyDispenser, List<Candy>>, input: Input): Pair<CandyDispenser, List<Candy>> {
        val newRcd = processInput(acc.first, input)
        val newCandys = if (newRcd.candy != null)
            acc.second.plus(newRcd.candy)
        else
            acc.second
        return newRcd to newCandys
    }

    val initial = rcd to emptyList<Candy>()

    return inputs.fold(initial, ::op)
}



/*

import Control.Monad
import Data.Maybe
import Data.Foldable
import Data.Monoid

--It works but without monads

data Candy = Red| Green| Blue | Yellow | Black deriving (Eq,Ord,Enum,Show,Bounded)

type Seed = Int


nextInt :: Seed -> (Int, Seed)
nextInt x =
  let b = mod (x + 1234) 10000
      sq = b * b
      newSeed = div (mod sq 1000000) 100
      newVal = mod newSeed 1000
  in
    ( newVal, newSeed)



randoms ::  Seed -> [Int]
randoms r =
   let ni = nextInt r
   in
       (fst ni) : (randoms (snd ni))


toCandy :: Int -> Candy
toCandy x = toEnum $ mod x 5

succCandy :: Candy -> Candy
succCandy x = toCandy $ (fromEnum x) + 1


nextCandy :: Seed -> (Candy, Seed)
nextCandy s = let (x, ns) = nextInt s
              in (toCandy x, ns)





--nothing about random here, just a wrapper on a function that change a state and extract a value
newtype StateChanger mystate val = StateChanger {runState :: mystate -> (val, mystate) }


instance Functor (StateChanger rs) where
    fmap f (StateChanger g) = StateChanger $ \s -> let (a, newState) = g s
                                                       b = f a
                                                   in  (b, newState)

instance Applicative (StateChanger rs) where
  pure a = StateChanger $ \s -> (a, s)
  StateChanger f <*> StateChanger x = StateChanger $ \s -> let (a, s1) = x s
                                                               (fab, s2) = f s1
                                                               b = fab a
                                                           in  (b, s2)

instance Monad (StateChanger rs) where
    (StateChanger h) >>= f = StateChanger $ \s -> let (a, newState) = h s
                                                      (StateChanger g) = f a
                                                  in  g newState



data Input = Coin | Turn

data CandyDispenser = Locked Int Int | Unlocked Int Int | Empty Int deriving Show

data CandyDispenser = CandyDispenser CandyDispenser Seed deriving Show


processInput :: CandyDispenser -> Input -> CandyDispenser
processInput (Locked candies coins) Coin = Unlocked candies (coins + 1)
processInput (Unlocked candies coins ) Turn = if candies > 1 then Locked (candies - 1) coins
                                                            else Empty coins
processInput (Empty coins) _ = Empty coins
processInput cd _ = cd


processInputs :: CandyDispenser -> [Input] -> CandyDispenser
processInputs cd inputs = last $ map (\i -> processInput cd i) inputs



getCandy :: CandyDispenser -> (Maybe Candy, CandyDispenser)
getCandy (CandyDispenser cd seed) = case cd of
                (Locked _ _) -> let (nc, ns) = nextCandy seed
                                  in  (Just nc, CandyDispenser cd ns)
                _ -> (Nothing, CandyDispenser cd seed)


candyFromDispenser :: CandyDispenser -> Input -> ( Maybe Candy, CandyDispenser)
candyFromDispenser (CandyDispenser cd seed) i = let ncd = processInput cd i
                                                       nrcd = CandyDispenser ncd seed
                                                   in getCandy nrcd


cfd :: ([Maybe Candy], CandyDispenser) -> Input -> ([Maybe Candy], CandyDispenser)
cfd (mcs, rcd) i =              let ( mc, nrcd) = candyFromDispenser rcd i
                                in (mc : mcs, nrcd)

candiesFromDispenser :: CandyDispenser -> [Input] -> ([Candy], CandyDispenser)
candiesFromDispenser cd is = let (candies, finalCd) = foldl cfd ([Nothing], cd) is
                             in (Data.Maybe.mapMaybe id candies, finalCd)



rndCandyDispenserS :: StateChanger CandyDispenser  (Maybe Candy)
rndCandyDispenserS = StateChanger getCandy

rndCandyS :: StateChanger Seed Candy
rndCandyS = StateChanger nextCandy

pushCandyState :: Seed -> StateChanger Seed ()
pushCandyState s = StateChanger $ \xs -> ( (), s)



threeCandies :: StateChanger Seed (Candy,Candy,Candy)
threeCandies = do
    a <- rndCandyS
    b <- rndCandyS
    c <- rndCandyS
    return (a,b,c)



threeCandiesD  :: StateChanger CandyDispenser (Maybe Candy,Maybe Candy,Maybe Candy)
threeCandiesD = do
    a <- rndCandyDispenserS
    b <- rndCandyDispenserS
    c <- rndCandyDispenserS
    return (a,b,c)


main = do

  print $ (Just 5) <|> Nothing

  print $ mappend (Sum 4.6) (Sum 7.2)

  let l = [1,3,4,7]

  print $ foldMap (Sum) l


  print . take 15 $ randoms (1)

  print $ runState threeCandies 51



  putStrLn "Candy Dispenser"
  putStrLn "----"
  -- print $ processInput (Locked 2 0) Coin
  -- print $ processInput (Unlocked 2 1) Turn
  -- print $ processInput (Locked 1 1) Coin
  -- print $ processInput (Unlocked 1 2) Turn
  -- print $ processInput (Empty 2) Coin

  -- print $ processInputs (Locked 10 0) [Coin, Turn, Coin, Turn, Coin, Turn]
  let rcd = (CandyDispenser (Locked 10 0) 3654)
  -- print $ candyFromDispenser rcd Coin

  -- let (rcd1, c1) = candyFromDispenser rcd Coin
  -- let (rcd2, c2) = candyFromDispenser rcd1 Turn
  -- let (rcd3, c3) = candyFromDispenser rcd2 Coin
  -- let (rcd4, c4) = candyFromDispenser rcd3 Turn

  -- print [rcd, rcd1, rcd2, rcd3, rcd4]
  -- print [c1,c2,c3,c4]

  -- print $ nextCandy 8925

  print $ candiesFromDispenser rcd [Coin, Turn, Coin, Turn, Coin, Turn, Coin, Turn, Coin, Turn, Coin, Turn]

  putStrLn "---- Monads"
  print $ runState threeCandiesD rcd







*/