import { Link } from '@tanstack/react-router';
import { Plus } from 'lucide-react';
import { Button } from '@/components/ui/button';

export function DesktopHeader() {
  return (
    <header
      className={`
        sticky top-0 z-50 w-full bg-cyan-950 px-2 text-slate-50 shadow-md
      `}
      data-testid="desktop-header"
    >
      <div
        className={`
          mx-auto flex max-w-[1200px] items-center justify-between gap-2 py-3
        `}
      >
        <Link to="/" className="text-3xl font-black tracking-tighter uppercase">
          OLX
        </Link>
        <div>
          <Button
            asChild
            size="default"
            className={`
              bg-white font-bold text-slate-950
              hover:bg-slate-200
            `}
          >
            <Link to="/offers/create">
              <Plus />
              Add offer
            </Link>
          </Button>
        </div>
      </div>
    </header>
  );
}
