import { createFileRoute } from '@tanstack/react-router';
import { useEffect } from 'react';

export const Route = createFileRoute('/')({
  component: App,
});

function App() {
  const d = 1;

  useEffect(() => {}, []);

  return <div className="text-center"></div>;
}
