package com.gamasoft.arrow.monads


enum class Candy { Red, Green, Blue, Yellow, Black }

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




/*
import Control.Monad
import Data.Maybe

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


setSeedCandy :: Seed -> ((), Seed)
setSeedCandy s = ( (), s)


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

--Input -> CandyDispenser -> (Candy, CandyDispenser)
processInput :: CandyDispenser -> Input -> CandyDispenser
processInput (Locked candies coins) Coin = Unlocked candies (coins + 1)
processInput (Unlocked candies coins ) Turn = if candies > 1 then Locked (candies - 1) coins
else Empty coins
processInput (Empty coins) _ = Empty coins
processInput cd _ = cd


processInputs :: CandyDispenser -> [Input] -> CandyDispenser
processInputs cd inputs = last $ map (\i -> processInput cd i) inputs



getCandy :: CandyDispenser -> Maybe Candy
getCandy cd = case cd of
(Unlocked _ _) -> Just Blue
_ -> Nothing


candyFromDispenser :: CandyDispenser -> Input -> (CandyDispenser, Maybe Candy)
candyFromDispenser cd i = let ncd = processInput cd i
candy = getCandy ncd
in (ncd, candy)

--CandyDispenser record with Seed state as well
-- CandyDispenser -> Input -> (CandyDispenser (Maybe Candy))


rndCandyState :: StateChanger Seed Candy
rndCandyState = StateChanger nextCandy

pushCandyState :: Seed -> StateChanger Seed ()
pushCandyState s = StateChanger $ \xs -> ( (), s)

fourCandies :: Seed -> StateChanger Seed (Candy,Candy,Candy,Candy)
fourCandies s = do
pushCandyState s
a <- rndCandyState
b <- rndCandyState
c <- rndCandyState
d <- rndCandyState
return (a,b,c,d)


threeCandies :: StateChanger Seed (Candy,Candy,Candy)
threeCandies = do
a <- rndCandyState
b <- rndCandyState
c <- rndCandyState
return (a,b,c)


main = do
print $ runState threeCandies 5

print $ runState (fourCandies 7) 5



putStrLn "Candy Dispenser"
putStrLn "----"
print $ processInput (Locked 2 0) Coin
print $ processInput (Unlocked 2 1) Turn
print $ processInput (Locked 1 1) Coin
print $ processInput (Unlocked 1 2) Turn
print $ processInput (Empty 2) Coin

print $ processInputs (Locked 10 0) [Coin, Turn, Coin, Turn, Coin, Turn]




print . take 10 $ randoms (1) [7, 5, 4]
let mr = StateChanger nextInt

let (a, s1) = runState mr 31
let (b, s2) = runState mr s1


print a
print $ toCandy a
print b
print $ toCandy b


let (ca, sa1) = runState rndCandyState 45
let (cb, sa2) = runState rndCandyState s1
print [ca, cb]


print $ runState (fmap (\ c -> (c, (succCandy c), (succCandy (succCandy c)))) rndCandyState)  6


--  g <- getStdGen
--  print $ take 10 (randoms g :: [Candy])

*/