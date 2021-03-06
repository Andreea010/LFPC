package Lab1;

enum RG implements State {
    S {
        @Override
        public State next(Input word) {
            switch(word.read()) {
                case 'a': return B;
                default: return Output.Invalid;
            }
        }
    },
    B {
        @Override
        public State next(Input word) {
            switch(word.read()) {
                case 'b': return S;
                case 'a': return C;
                case 'c': return Output.Valid;
                default: return Output.Invalid;
            }
        }
    },
    C {
        @Override
        public State next(Input word) {
            switch(word.read()) {
                case 'b': return D;
                default: return Output.Invalid;
            }
        }
    },
    D {
        @Override
        public State next(Input word) {
            switch(word.read()) {
                case 'c': return Output.Valid;
                case 'a': return C;
                default: return Output.Invalid;
            }
        }
    };

    public abstract State next(Input word);
}
