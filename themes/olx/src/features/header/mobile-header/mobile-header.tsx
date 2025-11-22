import { Link } from '@tanstack/react-router';
import { CirclePlus, Search } from 'lucide-react';
import type { ElementType } from 'react';

export function MobileNav() {
  return (
    <nav
      className="fixed bottom-0 left-0 z-50 w-full border-t border-border bg-background p-2"
      data-testid="mobile-navigation"
    >
      <div className="flex items-center justify-center gap-2">
        <Link to="/" className="group block flex-1">
          <MobileMenuNavItem icon={Search} label="Search" />
        </Link>
        <Link to="/offers/create" className="group block flex-1">
          <MobileMenuNavItem icon={CirclePlus} label="Add offer" />
        </Link>
      </div>
    </nav>
  );
}

type MobileMenuNavItemProps = {
  icon: ElementType;
  label: string;
};

function MobileMenuNavItem({ icon: Icon, label }: MobileMenuNavItemProps) {
  return (
    <div
      className={
        'flex flex-col gap-1 justify-center items-center transition-colors text-muted-foreground group-[.active]:text-primary'
      }
    >
      <Icon className="size-6" />
      <span className="text-sm font-medium leading-none">{label}</span>
    </div>
  );
}
