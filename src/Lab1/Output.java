package Lab1;

enum Output implements FinalState {
    Invalid {
        @Override
        public State next(Input word) {
            return Invalid;
        }
    },
    Valid {
        @Override
        public State next(Input word) {
            return Valid;
        }
    }
}
